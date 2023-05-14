package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
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
    private final Map<Long, Film> films= new HashMap<>();
    private long id= 0;


    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @ResponseBody
    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @ResponseBody
    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    private void validateFilm(Film film) {
        if (film.getName().isEmpty() ||
                film.getDescription().isEmpty() ||
                film.getDescription().length() > 200 ||
                LocalDate.parse(film.getReleaseDate(), DateTimeFormatter.ISO_DATE).isBefore(LocalDate.of(1895, 12, 28)) ||
                film.getDuration() <= 0) {
            throw new ValidationException("Данные не верны");
        }
    }
}