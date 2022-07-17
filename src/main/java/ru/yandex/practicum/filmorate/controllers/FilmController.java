package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class FilmController {
    private final LocalDate releaseLimit = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        filmControllerPostValidate(film);
        filmService.create(film);
        log.info("Создан объект Film: {}", film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PutMapping("/films")
    public Film filmUpdate(@RequestBody Film film) {
        filmControllerPutValidate(film);
        filmService.updateFilm(film);
        log.info("Данные Фильма обновлены: {}", film);
        return film;
    }

    @GetMapping("/films/{filmId}")
    public Film getFilm(@PathVariable Integer filmId) {
        filmIdValidate(filmId);
        log.info("Получен Фильм с id = {}", filmId);
        return filmService.getFilm(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public String addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmIdValidate(filmId);
        filmService.addLike(filmId, userId);
        log.info("Пользователи с id = {} поставил лайк фильму с id = {}", userId, filmId);
        return String.format("Пользователи с id = %d поставил лайк фильму с id = %d", userId, filmId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public String deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmIdValidate(filmId);
        filmService.deleteLike(filmId, userId);
        log.info("Пользователь с id = {} удалил лайк фильму с id = {}", userId, filmId);
        return String.format("Пользователи с id = %d удалил лайк фильму с id = %d", userId, filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count",
            defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        log.info("Получено {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }

    public Map<Integer, Film> getFilmsMap() {
        return filmService.getFilmsMap();
    }

    private void filmIdValidate(Integer filmId) {
        if (filmId == null) {
            log.debug("Некорректный Id при вводе запроса");
            throw new ValidationException("Id в запросе не может быть пустым.");
        }
        if (!getFilmsMap().containsKey(filmId)) {
            log.debug("Попытка получить фильм с несуществующим id");
            throw new NotFoundException("Фильм с таким id не найден.");
        }
    }

    private void filmControllerPostValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(releaseLimit)) {
            log.debug("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }
    }

    private void filmControllerPutValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(releaseLimit)) {
            log.debug("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }
        if (!filmService.getFilmsMap().containsKey(film.getId())) {
            log.debug("Попытка изменить фильм c несуществующим id");
            throw new NotFoundException("Фильм с таким id не найден");
        }
    }
}
