package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film getFilmById(Integer filmId);

    List<Film> getPopularFilms(Integer count);

    List<Film> getAllFilms();

    void updateFilm(Film film);
}
