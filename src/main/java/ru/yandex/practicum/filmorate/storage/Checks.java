package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@Component
public class Checks {
    private final JdbcTemplate jdbcTemplate;

    public Checks(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected void checkFilmIdInDB(Integer filmId) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("select film_id from films where film_id = ?", filmId);
        if (!idRows.next()) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
    }

    protected void checkUserIdInDB(Integer userId) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("select user_id from users where user_id = ?", userId);
        if (!idRows.next()) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }
}
