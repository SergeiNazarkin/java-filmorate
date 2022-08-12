package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.CheckUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
public class DbFilmRepository implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepository genreRepository;
    private final CheckUtils checkUtils;

    public DbFilmRepository(JdbcTemplate jdbcTemplate, GenreRepository genreRepository, CheckUtils checkUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreRepository = genreRepository;
        this.checkUtils = checkUtils;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME,MPA_ID, DESCRIPTION, RELEASE_DATE, DURATION)" +
                " values (?, ?, ?, ?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setInt(2, film.getMpa().getId());
            stmt.setString(3, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(releaseDate));
            }
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        genreRepository.setFilmGenre(film);
        return film;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        checkUtils.checkFilmIdInDB(filmId);
        final String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.MPA_ID, MPA.MPA_NAME," +
                " DESCRIPTION,RELEASE_DATE, DURATION " +
                "from FILMS as f Join MPA ON f.MPA_ID = MPA.MPA_ID where FILM_ID = ?";

        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, filmId);
        if (films.size() != 1) {
            return null;
        }
        return films.get(0);
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.MPA_ID, MPA.MPA_NAME, DESCRIPTION, RELEASE_DATE, DURATION" +
                " from FILMS as f Join MPA ON f.MPA_ID = MPA.MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public void updateFilm(Film film) {
        checkUtils.checkFilmIdInDB(film.getId());
        String sqlQuery = "update FILMS set FILM_NAME= ?, MPA_ID = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?" +
                " where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        genreRepository.setFilmGenre(film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, m.MPA_NAME\n" +
                "FROM FILMS f\n" +
                "         Left JOIN LIKES L on f.FILM_ID = l.FILM_ID\n" +
                "         JOIN MPA M on f.MPA_ID = M.MPA_ID\n" +
                "GROUP BY f.film_id\n" +
                "ORDER BY COUNT(l.user_id) DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film newFilm = new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION")
        );
        newFilm.getGenres().addAll(genreRepository.loadFilmGenre(newFilm));
        return newFilm;
    }
}
