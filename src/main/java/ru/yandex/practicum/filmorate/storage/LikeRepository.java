package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Checks check;
    public LikeRepository(JdbcTemplate jdbcTemplate, ru.yandex.practicum.filmorate.storage.Checks checks) {
        this.jdbcTemplate = jdbcTemplate;
        this.check = checks;
    }

    public void deleteLike(Integer filmId, Integer userId) {
        check.checkFilmIdInDB(filmId);
        check.checkUserIdInDB(userId);
        String sqlQuery = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void addLike(Integer filmId, Integer userId) {
        check.checkFilmIdInDB(filmId);
        check.checkUserIdInDB(userId);
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}


