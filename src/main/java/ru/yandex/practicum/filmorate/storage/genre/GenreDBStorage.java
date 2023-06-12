package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::genreMapping).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(long id) throws DataNotFoundException {
        try {
            String sql = "SELECT * FROM GENRES WHERE ID = ?";
            return jdbcTemplate.queryForObject(sql, this::genreMapping, id);
        } catch (RuntimeException e) {
            throw new DataNotFoundException("Genre не найден");
        }
    }

    private Genre genreMapping(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("ID"),
                rs.getString("NAME")
        );
    }
}
