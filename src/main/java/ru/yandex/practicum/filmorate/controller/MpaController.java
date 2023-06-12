package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class MpaController {
    MpaStorage mpaStorage;

    @GetMapping("/mpa")
    public Collection<Mpa> getAllMpa() {
        log.info("Получен запрос на получение всех рейтингов");
        return mpaStorage.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable Integer id) {
        log.info("Получен запрос на получение рейтинга с ID={}", id);
        return mpaStorage.getMpaById(id);
    }
}
