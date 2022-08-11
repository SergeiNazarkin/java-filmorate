package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable Integer genreId) {
        idValidate(genreId);
        log.info("Получен жанр с id = {}", genreId);
        return genreRepository.getById(genreId);
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        List<Genre> genreList = genreRepository.getAllGenres();
        log.info("Текущее количество жанров: {}", genreList.size());
        return genreList;
    }

    private void idValidate(Integer genreId) {
        if (genreId == null) {
            log.debug("Некорректный Id при вводе запроса");
            throw new ValidationException("Id в запросе не может быть пустым.");
        }
    }
}
