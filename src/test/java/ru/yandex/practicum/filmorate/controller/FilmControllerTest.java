package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

public class FilmControllerTest {

    FilmStorage filmStorage;
    UserStorage userStorage;
    private FilmService filmService;
    private Film film;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        film = new Film();
        film.setName("Гарри Поттер и философский камень");
        film.setDescription("Мальчик-сирота Гарри Поттер узнает, что он - волшебник, " +
                "и начинает свое обучение в Хогвартсе, школе волшебства и магии.");
        film.setReleaseDate(LocalDate.of(2001, 11, 16));
        film.setDuration(152);
        film.setId(1);
    }


    @Test
    public void testAddFilm() {
        Film film1 = filmController.create(film);
        assertEquals(film, film1, "Неверный ответ при передаче фильма");
        assertEquals(1, filmController.getFilms().size(), "Неверное колличество фильмов");
    }


    @Test
    public void testEmptyFilmName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }


    @Test
    public void testNameLength() {
        film.setDescription(film.getDescription() + film.getDescription());
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }


    @Test
    public void testEmptyDescription() {
        film.setDescription("");
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }


    @Test
    public void testIncorrectRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }


    @Test
    public void testIncorrectDurationNull() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }


    @Test
    public void testIncorrectDurationIsNegative() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}