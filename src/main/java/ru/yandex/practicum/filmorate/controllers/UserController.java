package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    // private static final Logger log = LoggerFactory.getLogger(UserController.class);
    protected final Map<Integer, User> usersMap = new HashMap<>();
    int idGen = 0;

    @PostMapping()
    public User create(@RequestBody @Valid User user) {
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

        idGen++;
        user.setId(idGen);
        usersMap.put(idGen, user);
        log.info("Создан объект User: {}", user);
        System.out.println(usersMap.get(1));
        return user;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        if (!usersMap.isEmpty()) {
            for (User user : usersMap.values()) {
                usersList.add(user);
            }
        }
        log.info("Текущее количество пользователе: {}", usersList.size());
        return usersList;
    }

    @PutMapping()
    public User updateUser(@RequestBody @Valid User user) {
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
        if (!usersMap.containsKey(user.getId())) {
            log.debug("Попытка изменить пользователю c несуществующим id");
            throw new ValidationException("Пользователь с таким id не найден.");
        }
        usersMap.put(user.getId(), user);
        log.info("Данные пользователя обновлены: {}", user);
        return user;
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



