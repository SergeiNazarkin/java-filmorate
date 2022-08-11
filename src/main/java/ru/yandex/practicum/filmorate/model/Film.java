package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private Integer id;
    private String name;
    private Mpa mpa;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Genre> genres = new HashSet<>();

    public Film(Integer id, String name, Mpa mpa, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.mpa = mpa;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

