package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserFriendService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.CheckUtils;
import ru.yandex.practicum.filmorate.storage.DBUserRepository;
import ru.yandex.practicum.filmorate.storage.UserFriendRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerJUnitTest {
    private UserController userController;

    @BeforeEach
    public void prepareTest() {
        this.userController = new UserController(new UserService(new DBUserRepository(new CheckUtils(new JdbcTemplate()),
                new JdbcTemplate())), new UserFriendService(new UserFriendRepository(new JdbcTemplate(),
                new CheckUtils(new JdbcTemplate()))));
    }

    @Test
    @DisplayName("Проверка валидации при создании пользователя с пустым логином")
    public void userCreateLoginIsEmptyTest() {
        User user = new User(1, "", "user@yandex.ru", "Иван",
                LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userController.create(user));

        assertEquals("Логин не может быть пустым.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создании пользователя с логином, содержащим пробел")
    public void userCreateLoginWithWhiteSpaceTest() {
        User user = new User(1, "iv an", "user@yandex.ru", "Иван",
                LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.create(user));

        assertEquals("Логин не может содержать пробел.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создании пользователя с будущей датой рождения")
    public void userCreateWithFutureDateTest() {
        User user = new User(1, "ivan", "user@yandex.ru", "Иван",
                LocalDate.of(2032, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.create(user));

        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создании пользователя с пустым email")
    public void userCreateEmailIsEmptyTest() {
        User user = new User(1, "ivan", "", "Иван", LocalDate.of(1985, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.create(user));

        assertEquals("Email не может быть пустым.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при создании пользователя с некорректным email")
    public void userCreateWithWrongFormatEmail() {
        User user = new User(1, "ivan", "useryandex.ru", "Иван",
                LocalDate.of(1985, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.create(user));

        assertEquals("Email должен содержать символ @.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении пользователя пустым логином")
    public void userUpdateLoginIsEmptyTest() {
        User user = new User(1, "", "user@yandex.ru", "Иван",
                LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.updateUser(user));

        assertEquals("Логин не может быть пустым.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении пользователя логином, содержащим пробел")
    public void userUpdateLoginWithWhiteSpaceTest() {
        User user = new User(1, "iv an", "user@yandex.ru", "Иван",
                LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.updateUser(user));

        assertEquals("Логин не может содержать пробел.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка валидации при обновлении пользователя будующей датой рождения")
    public void userUpdateWithFutureDateTest() {
        User user = new User(1, "ivan", "user@yandex.ru", "Иван",
                LocalDate.of(2032, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.updateUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }
}