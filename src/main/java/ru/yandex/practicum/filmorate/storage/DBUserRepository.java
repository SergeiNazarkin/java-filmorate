package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Primary
public class DBUserRepository implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public DBUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {

        String sqlQuery = "insert into USERS (LOGIN, EMAIL, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public User getUserById(Integer id) {
        checkUserIdInDB(id);
        final String sqlQuery = "select USER_ID, LOGIN, EMAIL, BIRTHDAY, USER_NAME " +
                "from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        checkUserIdInDB(userId);
        checkUserIdInDB(friendId);
        String sqlQuery = "insert into USERS_FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        checkUserIdInDB(userId);
        checkUserIdInDB(friendId);

        String sqlQuery = "delete from users_friends where user_id = ? AND friend_Id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select user_id, login, email, user_name, birthday from users";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public void updateUser(User user) {
        checkUserIdInDB(user.getId());
        String sqlQuery = "update users set login= ?, email = ?, user_name = ?, birthday = ? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getLogin()
                , user.getEmail()
                , user.getName()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public List<User> getUserFriendsList(Integer userId) {
        checkUserIdInDB(userId);
        String sqlQuery = "SELECT * From users Where user_id IN (" +
                "SELECT uf.friend_id from USERS_FRIENDS AS uf where uf.USER_ID=?)";
        final List<User> usersFriendsList;
        usersFriendsList = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
        return usersFriendsList;
    }

    @Override
    public List<User> getMutualFriendsList(Integer userId, Integer otherUserId) {
        String sqlQuery = "SELECT * From users Where user_id IN(" +
                "select uf.friend_id from USERS_FRIENDS AS uf where uf.USER_ID = ?)";
        final List<User> usersFriends = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
        final List<User> otherUserFriends = jdbcTemplate.query(sqlQuery, this::makeUser, otherUserId);
        List<User> mutualFriendsList = usersFriends.stream()
                .filter(otherUserFriends::contains).distinct().collect(Collectors.toList());
        return mutualFriendsList;
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("LOGIN"),
                rs.getString("EMAIL"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }

    protected void checkUserIdInDB(Integer userId) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("select user_id from users where user_id = ?", userId);
        if (!idRows.next()) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }
}
