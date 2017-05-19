package com.example.episodicshows.users.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(force = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @JsonInclude(NON_NULL)
    private Long id;
    @JsonProperty("email")
    private String email;

    @JsonCreator
    public UserEntity(@JsonProperty("email") String email) {
        this.email = email;
    }
}
