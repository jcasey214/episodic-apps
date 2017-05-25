package com.example.episodicshows.users;

import com.example.episodicshows.MessageQueueTestBase;
import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UsersRepo;
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
public class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UsersRepo usersRepo;

    @Test
    @Transactional
    @Rollback
    public void testGetUsersEndpoint() throws Exception {
        UserEntity user1 = new UserEntity("luke@example.com");

        usersRepo.save(user1);

        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email", equalTo("luke@example.com")))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testPostUsersEndpoint() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity user = new UserEntity("han@example.com");

        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", equalTo("han@example.com")))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

}
