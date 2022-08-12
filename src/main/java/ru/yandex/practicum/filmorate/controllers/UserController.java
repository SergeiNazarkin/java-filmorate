package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserFriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserFriendService userFriendService;


    @Autowired
    public UserController(UserService userService, UserFriendService userFriendService) {
        this.userService = userService;
        this.userFriendService = userFriendService;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        userControllerPostValidate(user);
        userService.createUser(user);
        log.info("Создан объект User: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Integer userId) {
        idValidate(userId);
        log.info("Получен пользователь с id = {}", userId);
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = userService.getAllUsers();
        log.info("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        userControllerPutValidate(user);
        userService.updateUser(user);
        log.info("Данные пользователя обновлены: {}", user);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public String addFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        idValidate(userId);
        idValidate(friendId);
        if (userId == friendId) {
            throw new ValidationException("Id не могут быть одинаковыми");
        }
        userFriendService.addFriends(userId, friendId);
        log.info("Пользователи с id = {} и с id = {} теперь друзья", friendId, userId);
        return String.format("Пользователи с id = %d и id = %d теперь друзья", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public String deleteFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        idValidate(userId);
        idValidate(friendId);
        userFriendService.deleteFriend(userId, friendId);
        log.info("Пользователи с id = {} и с id = {} теперь не друзья", friendId, userId);
        return String.format("Пользователь с id = %d отменил дружбу с id = %d", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriendsList(@PathVariable Integer userId) {
        idValidate(userId);
        List<User> userFriendsList = userService.getUserFriendsList(userId);
        log.info("Текущее количество друзей пользователя с id = {}: {}", userId, userFriendsList.size());
        return userFriendsList;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        idValidate(userId);
        idValidate(otherId);
        List<User> mutualFriendsList = userService.getMutualFriendsList(userId, otherId);
        log.info("Количество общих друзей - {}.", mutualFriendsList.size());
        return mutualFriendsList;
    }

    private void idValidate(Integer userId) {
        if (userId == null) {
            log.debug("Некорректный Id при вводе запроса");
            throw new ValidationException("Id в запросе не может быть пустым.");
        }
    }

    private void userControllerPostValidate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.debug("Попытка создания пользователя с пустым логином");
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (checkWhiteSpace(user.getLogin())) {
            log.debug("Логин содержит пробелы");
            throw new ValidationException("Логин не может содержать пробел.");
        }
        if (user.getName() == null || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Попытка создания пользователя с будущей датой рождения");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.debug("Попытка создания пользователя с пустым email");
            throw new ValidationException("Email не может быть пустым.");
        }
        if (!checkEmailCorrect(user.getEmail())) {
            log.debug("Email имеет некорректный формат");
            throw new ValidationException("Email должен содержать символ @.");
        }
    }

    private void userControllerPutValidate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.debug("Попытка изменить пользователю логин на пустой");
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (checkWhiteSpace(user.getLogin())) {
            log.debug("Логин содержит пробелы");
            throw new ValidationException("Логин не может содержать пробел.");
        }
        if (user.getName() == null || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Попытка изменить пользователю дату из будущего");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private boolean checkWhiteSpace(String s) {
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                return true;
            }
        }
        return false;
    }

    private boolean checkEmailCorrect(String s) {
        for (char c : s.toCharArray()) {
            if (c == '@') {
                return true;
            }
        }
        return false;
    }
}



