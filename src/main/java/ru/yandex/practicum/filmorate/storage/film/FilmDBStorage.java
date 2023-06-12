package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addLike(Long id, Long userId) {
        Film film = getFilmById(id);
        film.addLike(userId);
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) throws DataNotFoundException {
        String sqlFilmLikesById = "SELECT * FROM FILM_LIKES WHERE FILM_ID = ?";
        List<Long> filmLikesList = jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getLong("USER_ID"), id);

        String sqlDeleteLike = "DELETE FROM FILM_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlDeleteLike, id, userId);
    }

    private List<Long> getFilmLikes(Long id) {
        String sqlFilmLikesById = "SELECT * FROM FILM_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getLong("USER_ID"), id);
    }

    @Override
    public List<Film> getTopCountFilms(Integer count) {
        String getPopularQuery = "SELECT id, name, description, release_date, duration, rating_id " +
                "FROM films LEFT JOIN film_likes ON films.id = film_likes.film_id " +
                "GROUP BY films.id ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";

        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_Date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getFilmLikes(rs.getLong("id"))),
                        getMpaById(rs.getInt("rating_id")),
                        getFilmGenres(rs.getLong("id"))),
                count);
    }

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

    private Set<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT id, name FROM film_genres fg" +
                " INNER JOIN genres g ON fg.genre_id = g.id WHERE film_id = ?";
        return (jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("id"), rs.getString("name")), filmId)).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        film.setMpa(getMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(getGenreById(genre.getId()).getName());
            }
            deleteGenres(film.getId());
            addGenres(film);
        }
        return film;
    }

    private void addGenres(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    private void deleteGenres(long id) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", id);
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sql = "UPDATE FILMS SET " +
                "NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "RATING_ID = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        film.setMpa(getMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {

            film.setGenres(film.getGenres().stream()
                    .map(g -> getGenreById(g.getId()))
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            deleteGenres(film.getId());
            addGenres(film);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(getFilmLikes(rs.getLong("id"))),
                getMpaById(rs.getInt("rating_id")),
                getFilmGenres(rs.getLong("id")))
        );
    }

    @Override
    public Film getFilmById(Long id) {
        Film film;
        SqlRowSet films = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", id);
        if (films.first()) {
            Mpa mpa = getMpaById(films.getInt("rating_id"));
            Set<Genre> genres = getFilmGenres(id);
            film = new Film(
                    films.getLong("id"),
                    films.getString("name"),
                    films.getString("description"),
                    Objects.requireNonNull(films.getDate("release_date")).toLocalDate(),
                    films.getInt("duration"),
                    new HashSet<>(getFilmLikes(films.getLong("id"))),
                    mpa,
                    genres);
        } else {
            throw new DataNotFoundException("Фильм не найден!");
        }
        return film;
    }

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