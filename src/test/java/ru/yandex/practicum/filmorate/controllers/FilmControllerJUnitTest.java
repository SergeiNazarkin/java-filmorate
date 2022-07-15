package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerJUnitTest {
    private FilmController filmController;

    @BeforeEach
    public void prepareTest() {
        this.filmController = new FilmController();
    }

    @Test
    @DisplayName("Проверка успешного создания фильма при корректных данных")
    public void filmCreateTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при создании фильма с пустым названием")
    public void filmCreateNameIsEmptyTest() {
        Film film = new Film(1, "", "film1 description",
                LocalDate.of(2022, 6, 4), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.create(film));

        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создания фильма с датой до 1985-12-28")
    public void filmCreateDateBefore1985Test() {
        Film film = new Film(1, "belka", "film1 description",
                LocalDate.of(1895, 12, 27), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.create(film));

        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка успешного создания фильма с датой 1985-12-28")
    public void filmCreateDateEquals1985_12_28Test() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(1895, 12, 28), 120);
        filmController.create(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка успешного создания фильма с датой позже 1985-12-28")
    public void filmCreateDateAfter1985_12_28Test() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(1895, 12, 29), 120);
        filmController.create(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при создания фильма отрицательной продолжительностью")
    public void filmCreateDurationLessThanZeroTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), -120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.create(film));

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создания фильма продолжительностью равной 0")
    public void filmCreateDurationEqualsZeroTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), 0);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.create(film));

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 201-го символа")
    public void filmCreateDescriptionMoreThan200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам21.",
                LocalDate.of(2022, 6, 4), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.create(film));

        assertEquals("Описание фильма не должно превышать 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 200 символов")
    public void filmCreateDescriptionEquals200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам2.",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 199 символов")
    public void filmCreateDescriptionLessThan200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам.",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка успешного обновления фильма корректными данными")
    public void filmUpdateTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);
        Film film2 = new Film(1, "film2", "film1 description",
                LocalDate.of(2022, 6, 4), 120);
        assertEquals("film1", filmController.filmsMap.get(1).getName());
        filmController.filmUpdate(film2);

        assertEquals("film2", filmController.filmsMap.get(1).getName());
        assertEquals(film2, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с пустым названием")
    public void filmUpdateNameIsEmptyTest() {
        Film film = new Film(1, "", "film1 description",
                LocalDate.of(2022, 6, 4), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.filmUpdate(film));

        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с датой до 1985-12-28")
    public void filmUpdateDateBefore1985Test() {
        Film film = new Film(1, "belka", "film1 description",
                LocalDate.of(1895, 12, 27), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.filmUpdate(film));

        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с датой 1985-12-28")
    public void filmUpdateDateEquals1985_12_28Test() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(1895, 12, 28), 120);
        filmController.create(film);
        filmController.filmUpdate(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с датой позже 1985-12-28")
    public void filmUpdateDateAfter1985_12_28Test() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(1895, 12, 29), 120);
        filmController.create(film);
        filmController.filmUpdate(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с отрицательной продолжительностью")
    public void filmUpdateDurationLessThanZeroTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), -120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.filmUpdate(film));

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении фильма с продолжительностью равной 0")
    public void filmUpdateDurationEqualsZeroTest() {
        Film film = new Film(1, "film1", "film1 description",
                LocalDate.of(2022, 6, 4), 0);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.filmUpdate(film));

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 201-го символа")
    public void filmUpdateDescriptionMoreThan200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам21.",
                LocalDate.of(2022, 6, 4), 120);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> filmController.filmUpdate(film));

        assertEquals("Описание фильма не должно превышать 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 200 символов")
    public void filmUpdateDescriptionEquals200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам2.",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);
        filmController.filmUpdate(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }

    @Test
    @DisplayName("Проверка валидации при вводе в описание фильма 199 символов")
    public void filmUpdateDescriptionLessThan200SymbolsTest() {
        Film film = new Film(1, "film1", "Возможность нахождения поисковых ключей в тексте и" +
                " определения их количества полезна как для написания нового текста, так и для оптимизации уже" +
                " существующего. Расположенные ключевые слова по группам.",
                LocalDate.of(2022, 6, 4), 120);
        filmController.create(film);
        filmController.filmUpdate(film);

        assertEquals(film, filmController.filmsMap.get(1));
        assertNotNull(filmController.filmsMap.get(1));
    }
}