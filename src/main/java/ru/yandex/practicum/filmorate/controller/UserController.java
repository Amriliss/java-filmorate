package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController

public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long count = 0;

    @ResponseBody
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");
        if (isValidUser(user)) {
            user.setId(count + 1);
            users.put(user.getId(), user);

            return user;
        } else {
            throw new ValidationException("Ошибка добавления");
        }
    }

    @ResponseBody
    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя");
        if (isValidUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                count++;
                return user;
            } else {
                throw new ValidationException("Пользователь не найден");
            }
        } else {
            throw new ValidationException("Ошибка добавления");
        }
    }


    @ResponseBody
    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private boolean isValidUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.error("Ошибки в e-mail пользователя");
            return false;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Неправильный логин пользователя");
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неправильная дата рождения пользователя");
            return false;
        }
        return true;
    }
}