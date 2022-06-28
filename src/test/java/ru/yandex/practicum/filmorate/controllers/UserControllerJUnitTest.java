package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerJUnitTest {
    UserController uc;

    @BeforeEach
    public void prepareTest() {
        uc = new UserController();

    }

    @Test
    public void userCreateLoginIsEmptyTest() {
        User user = new User("user@yandex.ru", "", 1, "Иван", LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.create(user);
                    }
                });

        assertEquals("Логин не может быть пустым.", exception.getMessage());
    }

    @Test
    public void userCreateLoginWithWhiteSpaceTest() {
        User user = new User("user@yandex.ru", "iv an", 1, "Иван", LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.create(user);
                    }
                });

        assertEquals("Логин не может содержать пробел.", exception.getMessage());
    }

    @Test
    public void userCreateWithFutureDateTest() {
        User user = new User("user@yandex.ru", "ivan", 1, "Иван", LocalDate.of(2032, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.create(user);
                    }
                });

        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    public void userCreateWithWrongEmail() {
        User user = new User("", "ivan", 1, "Иван", LocalDate.of(1985, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.create(user);
                    }
                });

        assertEquals("Email не может быть пустым.", exception.getMessage());
    }

    @Test
    public void userCreateWithWrongFormatEmail() {
        User user = new User("useryandex.ru", "ivan", 1, "Иван", LocalDate.of(1985, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.create(user);
                    }
                });

        assertEquals("Email должен содержать символ @.", exception.getMessage());
    }

    @Test
    public void userUpdateLoginIsEmptyTest() {
        User user = new User("user@yandex.ru", "", 1, "Иван", LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.updateUser(user);
                    }
                });

        assertEquals("Логин не может быть пустым.", exception.getMessage());
    }


    @Test
    public void userUpdateLoginWithWhiteSpaceTest() {
        User user = new User("user@yandex.ru", "iv an", 1, "Иван", LocalDate.of(2022, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.updateUser(user);
                    }
                });

        assertEquals("Логин не может содержать пробел.", exception.getMessage());
    }

    @Test
    public void userUpdateWithFutureDateTest() {
        User user = new User("user@yandex.ru", "ivan", 1, "Иван", LocalDate.of(2032, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.updateUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    public void userUpdateWithWrongId() {
        User user = new User("user@yandex.ru", "ivan", 0, "Иван", LocalDate.of(1985, 6, 4));

        RuntimeException exception = assertThrows(RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        uc.updateUser(user);
                    }
                });

        assertEquals("Пользователь с таким id не найден.", exception.getMessage());
    }



}