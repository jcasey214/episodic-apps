package com.example.episodicshows.users.controller;

import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UsersRepo usersRepo;

    @Autowired
    public UsersController(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @GetMapping("/users")
    public Iterable<UserEntity> fetchAllUsers() {
        return usersRepo.findAll();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity createUser(@RequestBody UserEntity user) {
        return usersRepo.save(user);
    }
}
