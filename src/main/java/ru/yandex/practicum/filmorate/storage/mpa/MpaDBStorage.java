package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MpaDBStorage implements MpaStorage {
    JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        String sql = "SELECT * FROM RATINGS_MPA";
        return jdbcTemplate.query(sql, this::mpaMapping).stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaById(Integer id) throws DataNotFoundException {
        try {
            String sql = "SELECT * FROM RATINGS_MPA WHERE ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mpaMapping, id);
        } catch (RuntimeException e) {
            throw new DataNotFoundException("MPA не найден");
        }
    }

    private Mpa mpaMapping(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("ID"),
                rs.getString("NAME")
        );
    }
}
