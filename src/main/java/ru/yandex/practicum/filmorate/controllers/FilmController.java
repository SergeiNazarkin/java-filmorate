package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(ru.yandex.practicum.filmorate.controllers.FilmController.class);
    private Map<Integer, Film> filmsMap = new HashMap<>();
    LocalDate releaseLimit = LocalDate.of(1895,12,28);
    int idFilmGenerator = 0;

    @PostMapping()
    public Film create(@RequestBody @Valid Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым и содержать пробелы.");
        }
        if (film.getReleaseDate().isBefore(releaseLimit)) {
            log.debug("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if(film.getDuration()<=0){
            log.debug("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if(film.getDescription().length()>200){
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }

        idFilmGenerator++;
        film.setId(idFilmGenerator);
        filmsMap.put(idFilmGenerator, film);
        log.info("Создан объект Film: {}", film);
        return film;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>();
        if (!filmsMap.isEmpty()) {
            for (Film film : filmsMap.values()) {
                filmsList.add(film);
            }
        }
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PutMapping()
    public Film changeFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым и содержать пробелы.");
        }
        if (film.getReleaseDate().isBefore(releaseLimit)) {
            log.debug("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if(film.getDuration()<=0){
            log.debug("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if(film.getDescription().length()>200){
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }
        if (!filmsMap.keySet().contains(film.getId())) {
            log.debug("Попытка изменить фильм c несуществующим id");
            throw new ValidationException("Фильм с таким id не найден");
        }
        filmsMap.put(film.getId(), film);
        log.info("Данные Фильма обновлены: {}", film);
        return film;
    }

}
