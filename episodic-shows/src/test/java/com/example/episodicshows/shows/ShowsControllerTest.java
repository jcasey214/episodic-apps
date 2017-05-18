package com.example.episodicshows.shows;

import com.example.episodicshows.shows.data.entity.ShowEntity;
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
        ObjectMapper objectMapper = new ObjectMapper();
        ShowEntity show = new ShowEntity("stranger things");

        MockHttpServletRequestBuilder request = post("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(show));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("stranger things")))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

}
