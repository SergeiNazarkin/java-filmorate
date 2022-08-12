package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.utils.CheckUtils;

@Repository
public class UserFriendRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CheckUtils checkUtils;

    public UserFriendRepository(JdbcTemplate jdbcTemplate, CheckUtils checkUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkUtils = checkUtils;
    }

    public void addFriend(Integer userId, Integer friendId) {
        checkUtils.checkUserIdInDB(userId);
        checkUtils.checkUserIdInDB(friendId);
        String sqlQuery = "insert into USERS_FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        checkUtils.checkUserIdInDB(userId);
        checkUtils.checkUserIdInDB(friendId);

        String sqlQuery = "delete from users_friends where user_id = ? AND friend_Id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
