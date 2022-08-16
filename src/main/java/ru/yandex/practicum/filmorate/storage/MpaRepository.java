package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    public MpaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getById(int id) {
        checkMpaIdInDB(id);
        final String sqlQuery = "select MPA_ID, MPA_NAME " +
                "from MPA where Mpa_ID = ?";
        final List<Mpa> genres = jdbcTemplate.query(sqlQuery, this::makeMpa, id);
        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }


    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }

    private void checkMpaIdInDB(Integer mpaId) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("select mpa_id from mpa where mpa_id = ?", mpaId);
        if (!idRows.next()) {
            throw new NotFoundException("MPA with id=" + mpaId + " not found");
        }
    }
}
