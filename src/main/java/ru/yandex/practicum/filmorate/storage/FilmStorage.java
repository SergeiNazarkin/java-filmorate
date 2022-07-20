package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void create(Film film);

    Map<Integer, Film> getFilmsMap();

    Film getFilmById(Integer filmId);

    List<Film> getAllFilms();

    void updateFilm(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
