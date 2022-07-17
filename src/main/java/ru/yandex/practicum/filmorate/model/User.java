package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    @NotBlank
    @Email
    private String email;
    private String login;
    private Integer id;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friendIds = new HashSet<>();

    public User(String email, String login, Integer id, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }
}
