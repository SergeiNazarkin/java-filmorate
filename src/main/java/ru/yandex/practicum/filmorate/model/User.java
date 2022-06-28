package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @NotBlank
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    private String login;
    private Integer id;
    private String name;
    private LocalDate birthday;
}
