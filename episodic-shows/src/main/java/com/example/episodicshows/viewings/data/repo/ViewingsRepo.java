package com.example.episodicshows.viewings.data.repo;

import com.example.episodicshows.viewings.data.entity.ViewingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.View;
import java.util.List;

@Repository
public interface ViewingsRepo extends CrudRepository<ViewingEntity, Long> {
    List<ViewingEntity> findAllByUserIdOrderByUpdatedAt(Long userId);
    List<ViewingEntity> findAll();
    ViewingEntity findByShowIdAndUserId(Long showId, Long userId);
}
