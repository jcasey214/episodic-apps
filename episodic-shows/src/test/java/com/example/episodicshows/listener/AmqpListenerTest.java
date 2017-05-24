package com.example.episodicshows.listener;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.repo.EpisodesRepo;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import com.example.episodicshows.shows.service.ShowService;
import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UsersRepo;
import com.example.episodicshows.viewings.data.entity.ViewingEntity;
import com.example.episodicshows.viewings.data.repo.ViewingsRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AmqpListenerTest {

    @Autowired
    AmqpListener listener;
    @MockBean
    ShowService showService;
    @MockBean
    private EpisodesRepo episodesRepo;
    @MockBean
    private ShowsRepo showsRepo;
    @MockBean
    private ViewingsRepo viewingsRepo;
    @MockBean
    private UsersRepo usersRepo;

    @Test
    public void testReceiveMessage() throws Exception {
        Date createdAt = new Date();
        EpisodeEntity episode = EpisodeEntity.builder()
                .episodeNumber(2)
                .showId(1234L)
                .title("ice king kidnaps a princess")
                .seasonNumber(4)
                .id(99L)
                .build();

        when(showService.findEpisodeById(anyLong()))
                .thenReturn(episode);

        when(viewingsRepo.findByShowIdAndUserId(anyLong(), anyLong()))
                .thenReturn(null);

        when(viewingsRepo.save(any(ViewingEntity.class)))
                .thenReturn(ViewingEntity.builder().build());

        when(usersRepo.findOne(anyLong())).thenReturn(new UserEntity("email@example.com"));

        QueueMessage message = QueueMessage.builder()
                .createdAt(createdAt)
                .episodeId(99L)
                .userId(666L)
                .offset(7654)
                .build();

        ViewingEntity expectedViewing = ViewingEntity.builder()
                .episodeId(99L)
                .showId(1234L)
                .timecode(7654)
                .userId(666L)
                .updatedAt(createdAt)
                .build();

        listener.receiveMessage(message);

        verify(showService).findEpisodeById(99L);
        verify(viewingsRepo).findByShowIdAndUserId(1234L, 666L);
        verify(viewingsRepo).save(eq(expectedViewing));
    }


}
