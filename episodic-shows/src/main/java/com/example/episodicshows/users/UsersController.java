package com.example.episodicshows.users;

import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UsersController {

    private final UserRepo userRepo;

    @Autowired
    public UsersController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/users")
    public Iterable<UserEntity> fetchAllUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userRepo.save(user);
    }
}
