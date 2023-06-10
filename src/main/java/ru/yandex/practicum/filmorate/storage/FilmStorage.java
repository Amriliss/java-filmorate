package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    List<Film> getAllFilms();

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId) throws DataNotFoundException;

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    List<Genre> getGenres();

    Genre getGenreById(Integer id) throws DataNotFoundException;

    Mpa getMpaById(Integer id) throws DataNotFoundException;

    List<Mpa> getMpas();

    List<Film> getTopCountFilms(Integer count);
}
