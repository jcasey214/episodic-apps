package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.data.repo.EpisodesRepo;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UsersRepo;
import com.example.episodicshows.viewings.data.entity.ViewingEntity;
import com.example.episodicshows.viewings.data.repo.ViewingsRepo;
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

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
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
public class ViewingsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ViewingsRepo viewingsRepo;

    @Autowired
    ShowsRepo showsRepo;

    @Autowired
    EpisodesRepo episodesRepo;

    @Autowired
    UsersRepo usersRepo;

    @Test
    @Transactional
    @Rollback
    public void testGetRecentlyWatched() throws Exception {
        UserEntity user = usersRepo.save(new UserEntity("yoda@example.com"));
        ShowEntity show = showsRepo.save(new ShowEntity("Fraggle Rock"));
        EpisodeEntity episode = episodesRepo.save(
                EpisodeEntity.builder()
                        .showId(show.getId())
                        .seasonNumber(4)
                        .episodeNumber(20)
                        .build()
        );
        ViewingEntity viewing = viewingsRepo.save(
                ViewingEntity.builder()
                        .showId(show.getId())
                        .episodeId(episode.getId())
                        .userId(user.getId())
                        .updatedAt(Date.from(Instant.now()))
                        .timecode(1234567)
                        .build()
        );

        mockMvc.perform(get(String.format("/users/%s/recently-watched", user.getId().intValue())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].show.id", equalTo(show.getId().intValue())))
                .andExpect(jsonPath("$[0].show.name", equalTo(show.getName())))
                .andExpect(jsonPath("$[0].episode.seasonNumber", equalTo(episode.getSeasonNumber())))
                .andExpect(jsonPath("$[0].episode.episodeNumber", equalTo(episode.getEpisodeNumber())))
                .andExpect(jsonPath("$[0].episode.id", equalTo(episode.getId().intValue())))
                .andExpect(jsonPath("$[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$[0].timecode", equalTo(viewing.getTimecode())))
                .andExpect(jsonPath("$[0].episode.title", equalTo("S4 E20")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewings() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity user = usersRepo.save(new UserEntity("yoda@example.com"));
        ShowEntity show = showsRepo.save(new ShowEntity("Fraggle Rock"));
        EpisodeEntity episode = episodesRepo.save(
                EpisodeEntity.builder()
                        .showId(show.getId())
                        .seasonNumber(4)
                        .episodeNumber(20)
                        .build()
        );
        ViewingEntity viewing = ViewingEntity.builder()
                .episodeId(episode.getId())
                .updatedAt(Date.from(Instant.now()))
                .timecode(1234567)
                .build();

        MockHttpServletRequestBuilder request = patch(String.format("/users/%s/viewings", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(viewing));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        List<ViewingEntity> allViewings = viewingsRepo.findAll();
        assertThat(allViewings.size(), equalTo(1));
        assertThat(allViewings.get(0).getShowId(), equalTo(show.getId()));
        assertThat(allViewings.get(0).getEpisodeId(), equalTo(episode.getId()));
        assertThat(allViewings.get(0).getTimecode(), equalTo(viewing.getTimecode()));
        assertNotNull(allViewings.get(0).getUpdatedAt());
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewings_updatesViewingRowIfPresent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity user = usersRepo.save(new UserEntity("yoda@example.com"));
        ShowEntity show = showsRepo.save(new ShowEntity("Fraggle Rock"));
        EpisodeEntity episode = episodesRepo.save(
                EpisodeEntity.builder()
                        .showId(show.getId())
                        .seasonNumber(4)
                        .episodeNumber(20)
                        .build()
        );
        ViewingEntity viewing1 = ViewingEntity.builder()
                .episodeId(episode.getId())
                .updatedAt(Date.from(Instant.now()))
                .timecode(1234567)
                .build();

        MockHttpServletRequestBuilder request1 = patch(String.format("/users/%s/viewings", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(viewing1));

        ViewingEntity viewing2 = ViewingEntity.builder()
                .episodeId(episode.getId())
                .updatedAt(Date.from(Instant.now()))
                .timecode(7654321)
                .build();

        MockHttpServletRequestBuilder request2 = patch(String.format("/users/%s/viewings", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(viewing2));

        mockMvc.perform(request1)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(request2)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        List<ViewingEntity> allViewings = viewingsRepo.findAll();
        assertThat(allViewings.size(), equalTo(1));
        assertThat(allViewings.get(0).getShowId(), equalTo(show.getId()));
        assertThat(allViewings.get(0).getEpisodeId(), equalTo(episode.getId()));
        assertThat(allViewings.get(0).getTimecode(), equalTo(viewing2.getTimecode()));
        assertNotNull(allViewings.get(0).getUpdatedAt());
    }
}
