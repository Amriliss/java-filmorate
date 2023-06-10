package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id).addLike(userId);
    }

    public void deleteLike(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id).deleteLike(userId);
    }


    public List<Film> getTopCountFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
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
