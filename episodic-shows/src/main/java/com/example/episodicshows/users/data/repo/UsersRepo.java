package com.example.episodicshows.users.data.repo;

import com.example.episodicshows.users.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends CrudRepository<UserEntity, Long> {
}
