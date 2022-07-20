package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    protected Map<Integer, User> usersMap = new HashMap<>();
    private int idGen = 0;

    public Map<Integer, User> getUsersMap() {
        return usersMap;
    }

    @Override
    public void createUser(User user) {
        ++idGen;
        user.setId(idGen);
        usersMap.put(idGen, user);
    }

    @Override
    public User getUserById(Integer userId) {
        return usersMap.get(userId);
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        if (!usersMap.isEmpty()) {
            usersList.addAll(usersMap.values());
        }
        return usersList;
    }

    @Override
    public void updateUser(User user) {
        usersMap.put(user.getId(), user);
    }

    @Override
    public List<User> getUserFriendsList(Integer userId) {
        User user = usersMap.get(userId);
        Set<Integer> userFriendsSet = user.getFriendIds();
        List<User> userFriendsList = new ArrayList<>();
        for (Integer friendId : userFriendsSet) {
            userFriendsList.add(usersMap.get(friendId));
        }
        return userFriendsList;
    }

    @Override
    public List<User> getMutualFriendsList(User user, User otherUser) {
        Set<Integer> userFriendsIds = user.getFriendIds();
        Set<Integer> otherUserFriendsIds = otherUser.getFriendIds();
        Set<Integer> mutualFriendsSet = userFriendsIds.stream()
                .filter(otherUserFriendsIds::contains)
                .collect(Collectors.toSet());
        List<User> mutualFriendsList = new ArrayList<>();
        for (Integer userId : mutualFriendsSet) {
            mutualFriendsList.add(usersMap.get(userId));
        }
        return mutualFriendsList;
    }
}
