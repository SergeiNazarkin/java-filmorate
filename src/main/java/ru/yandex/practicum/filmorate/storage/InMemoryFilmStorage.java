package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    protected final Map<Integer, Film> filmsMap = new HashMap<>();
    private int idFilmGenerator = 0;

    public Map<Integer, Film> getFilmsMap() {
        return filmsMap;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return filmsMap.get(filmId);
    }

    @Override
    public void create(Film film) {
        ++idFilmGenerator;
        film.setId(idFilmGenerator);
        filmsMap.put(idFilmGenerator, film);
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>();
        if (!filmsMap.isEmpty()) {
            filmsList.addAll(filmsMap.values());
        }
        return filmsList;
    }

    @Override
    public void updateFilm(Film film) {
        filmsMap.put(film.getId(), film);
    }

    @Override
    public void addLike(Film film, User user) {
        film.getLikeUserIds().add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.getLikeUserIds().remove(user.getId());
    }
}
