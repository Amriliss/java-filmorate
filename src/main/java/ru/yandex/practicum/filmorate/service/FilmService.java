package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    FilmStorage filmStorage;

    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
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
        List<Film> films = filmStorage.getAllFilms();
        List<Film> topFilms = new ArrayList<>();
        films.sort(new Film.Compare());
        if (count > films.size()) {
            count = films.size();
        }
        for (int i = 0; i < count; i++) {
            topFilms.add(films.get(i));
        }
        return topFilms;
    }
}
