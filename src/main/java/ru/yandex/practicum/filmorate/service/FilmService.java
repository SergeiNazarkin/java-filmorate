package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbFilmRepository;

import java.util.List;

@Service
public class FilmService {
    private final DbFilmRepository dbFilmRepository;
    @Autowired
    public FilmService(DbFilmRepository dbFilmRepository) {
        this.dbFilmRepository = dbFilmRepository;
    }

    public Film getFilm(Integer filmId) {
        return dbFilmRepository.getFilmById(filmId);
    }

    public void create(Film film) {
        dbFilmRepository.create(film);
    }

    public List<Film> getAllFilms() {
        return dbFilmRepository.getAllFilms();
    }

    public void updateFilm(Film film) {
        dbFilmRepository.updateFilm(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        return dbFilmRepository.getPopularFilms(count);
    }
}
