package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Long, Film> films;
    private long id;

    public FilmController() {
        id = 0;
        films = new HashMap<>();
    }


    @GetMapping(value = "/films")
    public List<Film> getFilms() {
        log.info("Получение списка фильмов");
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Размер списка фильмов: {}", filmList.size());
        return filmList;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");
        if (!films.containsKey(film.getId())) {
            throw new DataNotFoundException("Фильм не найден");
        }

        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().isEmpty()) {
            throw new ValidationException("Описание фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно быть не более 200 символов");
        }

        LocalDate releaseDate = LocalDate.parse(film.getReleaseDate(), DateTimeFormatter.ISO_DATE);
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выпуска фильма должна быть после 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма должна быть положительным числом");
        }
    }
}