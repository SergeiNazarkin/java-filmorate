package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaRepository;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaRepository mpaRepository;

    public MpaController(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @GetMapping("/{mpaId}")
    public Mpa getGenre(@PathVariable Integer mpaId) {
        log.info("Получен MPA с id = {}", mpaId);
        return mpaRepository.getById(mpaId);
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = mpaRepository.getAllMpa();
        log.info("Текущее количество жанров: {}", mpaList.size());
        return mpaList;
    }
}
