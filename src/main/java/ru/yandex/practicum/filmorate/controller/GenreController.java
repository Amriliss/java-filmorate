package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class GenreController {

    GenreStorage genreStorage;

    @GetMapping("/genres")
    public Collection<Genre> getAllGenre() {
        log.info("Получен запрос на получение всех рейтингов");
        return genreStorage.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        log.info("Получен запрос на получение рейтинга с ID={}", id);
        return genreStorage.getGenreById(id);
    }
}
