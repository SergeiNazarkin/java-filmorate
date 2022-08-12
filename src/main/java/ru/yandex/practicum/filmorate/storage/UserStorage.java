package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void createUser(User user);

    User getUserById(Integer userId);

    List<User> getAllUsers();

    void updateUser(User user);

    List<User> getUserFriendsList(Integer userId);

    List<User> getMutualFriendsList(Integer userId, Integer otherUserId);
}
