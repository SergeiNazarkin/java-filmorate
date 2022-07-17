package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUser(Integer userId) {
        final User user = inMemoryUserStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        return user;
    }

    public void addFriends(Integer userId, Integer friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        checkUserInMap(friend, friendId);
        inMemoryUserStorage.addFriend(user, friend);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        checkUserInMap(friend, friendId);
        inMemoryUserStorage.deleteFriend(user, friend);
    }

    public void createUser(User user) {
        inMemoryUserStorage.createUser(user);
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        inMemoryUserStorage.updateUser(user);
    }

    public Map<Integer, User> getUsersMap() {
        return inMemoryUserStorage.getUsersMap();
    }

    public List<User> getUserFriendsList(Integer userId) {
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        return inMemoryUserStorage.getUserFriendsList(userId);
    }

    public List<User> getMutualFriendsList(Integer userId, Integer otherId) {
        User user = inMemoryUserStorage.getUserById(userId);
        checkUserInMap(user, userId);
        User otherUser = inMemoryUserStorage.getUserById(otherId);
        checkUserInMap(otherUser, otherId);
        return inMemoryUserStorage.getMutualFriendsList(user, otherUser);
    }

    private void checkUserInMap(User user, Integer userId) {
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }
}


