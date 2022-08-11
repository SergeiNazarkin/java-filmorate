package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    @NotBlank
    @Email
    private String email;
    private String login;
    private Integer Id;
    private String name;
    private LocalDate birthday;

    public User(Integer Id, String login, String email, String name, LocalDate birthday) {
        this.Id = Id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }
}
