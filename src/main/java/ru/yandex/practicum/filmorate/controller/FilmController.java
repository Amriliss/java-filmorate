package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получение списка фильмов");
        List<Film> filmList = new ArrayList<>(filmService.getAllFilms());
        log.info("Размер списка фильмов: {}", filmList.size());
        return filmList;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");
        validateFilm(film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");

        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Получение фильма по id {}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка фильму с id {} пользователем с id {}", id, userId);

        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка у фильма с id {} пользователем с id {}", id, userId);

        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopCountFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Получение ТОП-{} фильмов", count);

        return filmService.getTopCountFilms(count);
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