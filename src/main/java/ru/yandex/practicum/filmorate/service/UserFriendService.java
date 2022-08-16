package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserFriendRepository;

@Service
public class UserFriendService {
    private final UserFriendRepository userFriendRepository;

    @Autowired
    public UserFriendService(UserFriendRepository userFriendRepository) {
        this.userFriendRepository = userFriendRepository;
    }

    public void addFriends(Integer userId, Integer friendId) {
        userFriendRepository.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        userFriendRepository.deleteFriend(userId, friendId);
    }
}

