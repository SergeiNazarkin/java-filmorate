package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate releaseLimit = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;
    private final LikeService likeService;


    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        filmControllerValidate(film);
        filmService.create(film);
        log.info("Создан объект Film: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film film) {
        filmControllerValidate(film);
        filmService.updateFilm(film);
        log.info("Данные Фильма обновлены:\n {}", film);
        return film;
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Integer filmId) {
        filmIdValidate(filmId);
        log.info("Получен Фильм с id = {}", filmId);
        return filmService.getFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public String addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmIdValidate(filmId);
        likeService.addLike(filmId, userId);
        log.info("Пользователи с id = {} поставил лайк фильму с id = {}", userId, filmId);
        return String.format("Пользователи с id = %d поставил лайк фильму с id = %d", userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public String deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmIdValidate(filmId);
        likeService.deleteLike(filmId, userId);
        log.info("Пользователь с id = {} удалил лайк фильму с id = {}", userId, filmId);
        return String.format("Пользователи с id = %d удалил лайк фильму с id = %d", userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count",
            defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        log.info("Получено {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }

    private void filmIdValidate(Integer filmId) {
        if (filmId == null) {
            log.error("Некорректный Id при вводе запроса");
            throw new ValidationException("Id в запросе не может быть пустым.");
        }
    }

    private void filmControllerValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(releaseLimit)) {
            log.error("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if (film.getDescription().length() > 200) {
            log.error("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }
    }
}
