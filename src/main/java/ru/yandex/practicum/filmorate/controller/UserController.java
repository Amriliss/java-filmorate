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


    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");
        validateUser(user);

        user.setId(count++);
        users.put(user.getId(), user);
        return user;
    }


    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя");
        validateUser(user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            count++;
            return user;
        } else {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
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