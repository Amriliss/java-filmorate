package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя");
        validateUser(user);
        return userService.updateUser(user);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");
        validateUser(user);
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return new ArrayList<>(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Получение пользователя по id {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long friendId, @PathVariable Long id) {
        log.info("Добавление пользователю с id {} друга с id{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long friendId, @PathVariable Long id) {
        log.info("Удаление у пользователя с id {} друга с id{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        log.info("Получение всех друзей пользователя с id {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получение общих друзей пользователей с id {} и с id {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }


    private void validateUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.error("Ошибки в e-mail пользователя");
            throw new ValidationException("Неправильный e-mail пользователя");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Неправильный логин пользователя");
            throw new ValidationException("Неправильный логин пользователя");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неправильная дата рождения пользователя");
            throw new ValidationException("Неправильная дата рождения пользователя");
        }
    }
}