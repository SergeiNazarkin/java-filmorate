package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.utils.CheckUtils;

@Repository
public class LikeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CheckUtils checkUtils;

    public LikeRepository(JdbcTemplate jdbcTemplate, CheckUtils checkUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkUtils = checkUtils;
    }

    public void deleteLike(Integer filmId, Integer userId) {
        checkUtils.checkFilmIdInDB(filmId);
        checkUtils.checkUserIdInDB(userId);
        String sqlQuery = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void addLike(Integer filmId, Integer userId) {
        checkUtils.checkFilmIdInDB(filmId);
        checkUtils.checkUserIdInDB(userId);
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}


