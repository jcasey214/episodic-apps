package com.example.episodicevents.controller;

import com.example.episodicevents.data.repo.EventsRepo;
import com.example.episodicevents.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/episodic-events-test"

})
public class EventsControllerTest {

    private final ObjectMapper om = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EventsRepo eventsRepo;
    @MockBean
    RabbitTemplate rabbitTemplate;

    ArgumentCaptor<QueueMessage> argumentCaptor;

    @Before
    public void setup() {
        argumentCaptor = ArgumentCaptor.forClass(QueueMessage.class);

        doNothing()
                .when(rabbitTemplate)
                .convertAndSend(eq("my-exchange"), eq("my-routing-key"), argumentCaptor.capture());
    }

    @After
    public void cleanUp() {
        eventsRepo.deleteAll();
    }

    @Test
    public void testPost_withPlayEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "play");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745");

        JsonObject data = new JsonObject();
        data.addProperty("offset", 0);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        PlayPauseProgressEvent playPauseProgressEvent = (PlayPauseProgressEvent) all.get(0);

        assertThat(all.size(), equalTo(1));
        assertThat(playPauseProgressEvent.getShowId(), equalTo(987L));
        assertThat(playPauseProgressEvent.getType(), equalTo("play"));
        assertThat(playPauseProgressEvent.getEpisodeId(), equalTo(456L));
        assertThat(playPauseProgressEvent.getUserId(), equalTo(52L));
        assertThat(playPauseProgressEvent.getData().getOffset(), equalTo(0));
    }

    @Test
    public void testPost_withPauseEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "pause");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745");

        JsonObject data = new JsonObject();
        data.addProperty("offset", 1023);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        PlayPauseProgressEvent playPauseProgressEvent = (PlayPauseProgressEvent) all.get(0);

        assertThat(all.size(), equalTo(1));
        assertThat(playPauseProgressEvent.getShowId(), equalTo(987L));
        assertThat(playPauseProgressEvent.getType(), equalTo("pause"));
        assertThat(playPauseProgressEvent.getEpisodeId(), equalTo(456L));
        assertThat(playPauseProgressEvent.getUserId(), equalTo(52L));
        assertThat(playPauseProgressEvent.getData().getOffset(), equalTo(1023));
    }

    @Test
    public void testPost_withFastForwardEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "fastForward");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745");

        JsonObject data = new JsonObject();
        data.addProperty("startOffset", 4);
        data.addProperty("endOffset", 408);
        data.addProperty("speed", 2.5F);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        FastForwardRewindEvent fastForwardRewindEvent = (FastForwardRewindEvent) all.get(0);

        assertThat(all.size(), equalTo(1));
        assertThat(fastForwardRewindEvent.getShowId(), equalTo(987L));
        assertThat(fastForwardRewindEvent.getType(), equalTo("fastForward"));
        assertThat(fastForwardRewindEvent.getEpisodeId(), equalTo(456L));
        assertThat(fastForwardRewindEvent.getUserId(), equalTo(52L));
        assertThat(fastForwardRewindEvent.getData().getStartOffset(), equalTo(4));
        assertThat(fastForwardRewindEvent.getData().getEndOffset(), equalTo(408));
        assertThat(fastForwardRewindEvent.getData().getSpeed(), equalTo(2.5F));
    }

    @Test
    public void testPost_withRewindEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "rewind");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745");

        JsonObject data = new JsonObject();
        data.addProperty("startOffset", 987);
        data.addProperty("endOffset", 420);
        data.addProperty("speed", 1.25F);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        FastForwardRewindEvent fastForwardRewindEvent = (FastForwardRewindEvent) all.get(0);

        assertThat(all.size(), equalTo(1));
        assertThat(fastForwardRewindEvent.getShowId(), equalTo(987L));
        assertThat(fastForwardRewindEvent.getType(), equalTo("rewind"));
        assertThat(fastForwardRewindEvent.getEpisodeId(), equalTo(456L));
        assertThat(fastForwardRewindEvent.getUserId(), equalTo(52L));
        assertThat(fastForwardRewindEvent.getData().getStartOffset(), equalTo(987));
        assertThat(fastForwardRewindEvent.getData().getEndOffset(), equalTo(420));
        assertThat(fastForwardRewindEvent.getData().getSpeed(), equalTo(1.25F));
    }


    @Test
    public void testPost_withProgressEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "progress");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745Z");

        JsonObject data = new JsonObject();
        data.addProperty("offset", 378);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        PlayPauseProgressEvent playPauseProgressEvent = (PlayPauseProgressEvent) all.get(0);

        assertThat(all.size(), equalTo(1));
        assertThat(playPauseProgressEvent.getShowId(), equalTo(987L));
        assertThat(playPauseProgressEvent.getType(), equalTo("progress"));
        assertThat(playPauseProgressEvent.getEpisodeId(), equalTo(456L));
        assertThat(playPauseProgressEvent.getUserId(), equalTo(52L));
        assertThat(playPauseProgressEvent.getData().getOffset(), equalTo(378));

        assertThat(argumentCaptor.getValue().getEpisodeId(), equalTo(456L));
        assertThat(argumentCaptor.getValue().getOffset(), equalTo(378));
        assertThat(argumentCaptor.getValue().getUserId(), equalTo(52L));
    }

    @Test
    public void testPost_withScrubEvent() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("type", "scrub");
        requestBody.addProperty("userId", 52);
        requestBody.addProperty("showId", 987);
        requestBody.addProperty("episodeId", 456);
        requestBody.addProperty("createdAt", "2017-11-08T15:59:13.0091745");

        JsonObject data = new JsonObject();
        data.addProperty("startOffset", 92129);
        data.addProperty("endOffset", 92139);

        requestBody.add("data", data);

        Gson builder = new GsonBuilder().create();

        MockHttpServletRequestBuilder request = post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(builder.toJson(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Event> all = eventsRepo.findAll();

        ScrubEvent scrubEvent = (ScrubEvent) all.get(0);
        System.out.println(scrubEvent);

        assertThat(all.size(), equalTo(1));
        assertThat(scrubEvent.getShowId(), equalTo(987L));
        assertThat(scrubEvent.getType(), equalTo("scrub"));
        assertThat(scrubEvent.getEpisodeId(), equalTo(456L));
        assertThat(scrubEvent.getUserId(), equalTo(52L));
        assertThat(scrubEvent.getData().getStartOffset(), equalTo(92129));
        assertThat(scrubEvent.getData().getEndOffset(), equalTo(92139));
    }

    @Test
    public void testGetRecent() throws Exception {
        for (int i = 0; i < 30; i++) {
            eventsRepo.save(
                    Event.builder()
                            .createdAt(Date.from(Instant.parse("2015-11-08T15:59:13.0091745Z")))
                            .showId(542L)
                            .build());
        }

        eventsRepo.save(
                Event.builder()
                        .createdAt(Date.from(Instant.parse("2017-11-08T15:59:13.0091745Z")))
                        .showId(187L)
                        .build());

        ObjectMapper om = new ObjectMapper();


        MvcResult mvcResult = mockMvc.perform(get("/recent"))
                .andExpect(status().isOk())
                .andReturn();

        HashMap resultMap = om.readValue(mvcResult.getResponse().getContentAsString(), HashMap.class);
        List<Map<String, Object>> events = (ArrayList) resultMap.get("content");
        assertThat(events.size(), equalTo(20));
        assertThat(events.get(0).get("showId"), equalTo(187));
    }
}
