package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class GenreController {

    GenreService genreService;

    @GetMapping("/genres")
    public Collection<Genre> getAllGenre() {
        log.info("Получен запрос на получение всех рейтингов");
        return genreService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        log.info("Получен запрос на получение рейтинга с ID={}", id);
        return genreService.getGenreById(id);
    }
}
