package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
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
        validateUser(user);

        user.setId(count ++);
        users.put(user.getId(), user);
        return user;
    }

    @ResponseBody
    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя");
        validateUser(user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            count++;
            return user;
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @ResponseBody
    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.error("Ошибки в e-mail пользователя");
            throw new InvalidEmailException("Неправильный e-mail пользователя");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Неправильный логин пользователя");
            throw new InvalidLoginException("Неправильный логин пользователя");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неправильная дата рождения пользователя");
            throw new InvalidBirthdayException("Неправильная дата рождения пользователя");
        }
    }
}