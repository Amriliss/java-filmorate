package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private User user;
    private UserService userServise;
    UserStorage userStorage;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userServise = new UserService(userStorage);
        userController = new UserController(userServise);
        user = new User();
        user.setName("Гарри Поттер");
        user.setLogin("hpotter");
        user.setEmail("harry@hogwarts.com");
        user.setBirthday(LocalDate.of(1980, 7, 31));
        user.setId(1);
    }

    @Test
    public void testAddUserAllCorrect() {
        User user1 = userController.create(user);
        assertEquals(user, user1, "Неверный отчёт при передачи пользователя");
        assertEquals(1, userController.getAllUsers().size(), "Ошибка списока пользователей");
    }


    @Test
    public void testEmailIsEmpty() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getAllUsers().size(), "Ошибка списока пользователей");
    }


    @Test
    public void testIncorrectEmail() {
        user.setEmail("yandex.ru");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }


    @Test
    public void testIncorrectLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }


    @Test
    public void testIncorrectNameWithSpaces() {
        user.setLogin("Леонид Куравлёв");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }


    @Test
    public void testEmptyName() {
        user.setName("");
        User user1 = userController.create(user);
        assertEquals(user1.getName(), user.getLogin(), "Имя и логин пользователя не совпадают");
    }


    @Test
    public void testFutureBirthday() {
        user.setBirthday(LocalDate.parse(String.valueOf(LocalDate.now().plusDays(1))));
        assertThrows(ValidationException.class, () -> userController.create(user));
    }
}