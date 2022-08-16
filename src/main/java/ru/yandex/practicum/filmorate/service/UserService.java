package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DBUserRepository;

import java.util.List;

@Service
public class UserService {
    private final DBUserRepository dbUserRepository;

    @Autowired
    public UserService(DBUserRepository dbUserRepository) {
        this.dbUserRepository = dbUserRepository;
    }

    public User getUser(Integer userId) {
        return dbUserRepository.getUserById(userId);
    }

    public void createUser(User user) {
        dbUserRepository.createUser(user);
    }

    public List<User> getAllUsers() {
        return dbUserRepository.getAllUsers();
    }

    public void updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        dbUserRepository.updateUser(user);
    }

    public List<User> getUserFriendsList(Integer userId) {
        return dbUserRepository.getUserFriendsList(userId);
    }

    public List<User> getMutualFriendsList(Integer userId, Integer otherUserId) {
        return dbUserRepository.getMutualFriendsList(userId, otherUserId);
    }
}


