package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Map<Integer, Film> getFilmsMap() {
        return inMemoryFilmStorage.getFilmsMap();
    }

    public Film getFilm(Integer filmId) {
        final Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
        return film;
    }

    public void create(Film film) {
        inMemoryFilmStorage.create(film);
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void updateFilm(Film film) {
        inMemoryFilmStorage.updateFilm(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        checkFilmInMap(film, userId);
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        if (film.getLikeUserIds().contains(userId)) {
            throw new ValidationException("Пользователь с id=" + userId + " уже ставил лайк фильму c id=" + filmId);
        }
        film.setLikeCount(film.getLikeCount() + 1);
        inMemoryFilmStorage.addLike(film, user);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        checkFilmInMap(film, userId);
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        if (!film.getLikeUserIds().contains(userId)) {
            throw new ValidationException("Пользователь с id=" + userId + " еще не ставил лайк фильму c id=" + filmId);
        }
        film.setLikeCount(film.getLikeCount() - 1);
        inMemoryFilmStorage.deleteLike(film, user);
    }

    public List<Film> getPopularFilms(Integer count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikeCount() - o1.getLikeCount())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkFilmInMap(Film film, Integer filmId) {
        if (film == null) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
    }

    private void checkUserInMap(User user, Integer userId) {
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }
}
