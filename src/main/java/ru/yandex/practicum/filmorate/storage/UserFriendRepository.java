package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserFriendRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Checks check;

    public UserFriendRepository(JdbcTemplate jdbcTemplate, Checks check) {
        this.jdbcTemplate = jdbcTemplate;
        this.check = check;
    }

    public void addFriend(Integer userId, Integer friendId) {
        check.checkUserIdInDB(userId);
        check.checkUserIdInDB(friendId);
        String sqlQuery = "insert into USERS_FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        check.checkUserIdInDB(userId);
        check.checkUserIdInDB(friendId);

        String sqlQuery = "delete from users_friends where user_id = ? AND friend_Id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
