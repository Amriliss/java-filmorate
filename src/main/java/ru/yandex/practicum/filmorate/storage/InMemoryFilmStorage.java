package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();
    private final Map<Long, Film> films = new HashMap<>();
    private static long id = 0;


    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        if (films.containsKey(film.getId())) {
            throw new AlreadyExistException("Фильм уже существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Long id, Long userId) {
        getFilmById(id).addLike(userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        getFilmById(id).deleteLike(userId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new DataNotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new DataNotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(Integer id) throws DataNotFoundException {
        return null;
    }

    @Override
    public Mpa getMpaById(Integer id) throws DataNotFoundException {
        return null;
    }

    @Override
    public List<Mpa> getMpas() {
        return null;
    }

    @Override
    public List<Film> getTopCountFilms(Integer count) {
        return getAllFilms().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }


}
