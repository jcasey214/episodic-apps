package com.example.episodicshows.shows;

import com.example.episodicshows.MessageQueueTestBase;
import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.data.repo.EpisodesRepo;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost/episodic_shows_test?serverTimezone=UTC",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-title=com.mysql.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=verify",
        "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect",
        "flyway.enabled=true",
        "spring.jpa.properties.hibernate.show_sql=true",
        "spring.jpa.properties.hibernate.use_sql_comments=true",
        "spring.jpa.properties.hibernate.format_sql=true"
})
public class ShowsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ShowsRepo showsRepo;

    @Autowired
    EpisodesRepo episodesRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    @Rollback
    public void testGetShowsEndpoint() throws Exception {
        ShowEntity show1 = new ShowEntity("sense8");

        showsRepo.save(show1);

        mockMvc.perform(get("/shows").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", equalTo("sense8")))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testPostShowsEndpoint() throws Exception {
        ShowEntity show = new ShowEntity("stranger things");

        MockHttpServletRequestBuilder request = post("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(show));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("stranger things")))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetEpisodesEndpoint() throws Exception {
        ShowEntity show1 = new ShowEntity("got");
        ShowEntity show = showsRepo.save(show1);
        EpisodeEntity episode = EpisodeEntity.builder().showId(show.getId())
                .episodeNumber(4)
                .seasonNumber(2)
                .build();
        episodesRepo.save(episode);

        mockMvc.perform(get(String.format("/shows/%s/episodes", show.getId())).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seasonNumber", equalTo(2)))
                .andExpect(jsonPath("$[0].episodeNumber", equalTo(4)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].title", equalTo("S2 E4")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostEpisodesEndpoint() throws Exception {
        ShowEntity show1 = new ShowEntity("got");
        ShowEntity show = showsRepo.save(show1);
        EpisodeEntity episode = EpisodeEntity.builder().seasonNumber(6).episodeNumber(8).build();
        MockHttpServletRequestBuilder request = post(String.format("/shows/%s/episodes", show.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(episode));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seasonNumber", equalTo(6)))
                .andExpect(jsonPath("$.episodeNumber", equalTo(8)))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title", equalTo("S6 E8")));

        List<EpisodeEntity> episodes = episodesRepo.findAll();
        assertThat(episodes.size(), equalTo(1));
        assertThat(episodes.get(0).getShowId(), equalTo(show.getId()));
    }

}
