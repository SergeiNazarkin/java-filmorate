package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    public GenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getById(int id) {
        checkGenreIdInDB(id);
        final String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                "from Genres where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    public void setFilmGenre(Film film) {
        int filmId = film.getId();
        String sqlQuery = "DELETE FROM films_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        for (Genre genre : film.getGenres()) {
            String sqlQueryGenre = "INSERT INTO films_genres(film_id, genre_id) values (?,?)";
            jdbcTemplate.update(sqlQueryGenre, filmId, genre.getId());
        }
    }

    public List<Genre> loadFilmGenre(Film film) {
        int filmId = film.getId();
        String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                "from GENRES " +
                "WHERE GENRE_ID IN (SELECT GENRE_ID " +
                "FROM FILMS_GENRES " +
                "WHERE FILM_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }

    private void checkGenreIdInDB(Integer genreId) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("select genre_id from genres where genre_id = ?", genreId);
        if (!idRows.next()) {
            throw new NotFoundException("Genre with id=" + genreId + " not found");
        }
    }
}
