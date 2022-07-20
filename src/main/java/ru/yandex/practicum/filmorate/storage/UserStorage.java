package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void createUser(User user);

    User getUserById(Integer userId);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getAllUsers();

    void updateUser(User user);

    List<User> getUserFriendsList(Integer userId);

    List<User> getMutualFriendsList(User user, User otherUser);
}
