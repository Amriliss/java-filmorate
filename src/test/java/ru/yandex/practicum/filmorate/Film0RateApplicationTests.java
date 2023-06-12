package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDBStorage userStorage;
    private final FilmDBStorage filmStorage;
    private final FilmService filmService;
    private final UserService userService;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .name("First")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1981, 1, 11))
                .build();

        user2 = User.builder()
                .name("Second")
                .login("Second")
                .email("2@ya.ru")
                .birthday(LocalDate.of(1982, 2, 12))
                .build();

        user3 = User.builder()
                .name("Third")
                .login("Third")
                .email("3@ya.ru")
                .birthday(LocalDate.of(1983, 3, 13))
                .build();

        film1 = Film.builder()
                .name("First film")
                .description("First film description")
                .releaseDate(LocalDate.of(1975, 5, 5))
                .duration(111)
                .build();
        film1.setMpa(new Mpa(1, "G"));
        film1.setLikes(new HashSet<>());
        film1.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
                new Genre(1, "Комедия"))));

        film2 = Film.builder()
                .name("Second film")
                .description("Second film description.")
                .releaseDate(LocalDate.of(2000, 12, 15))
                .duration(222)
                .build();
        film2.setMpa(new Mpa(3, "PG-13"));
        film2.setLikes(new HashSet<>());
        film2.setGenres(new HashSet<>(List.of(new Genre(6, "Боевик"))));

        film3 = Film.builder()
                .name("Third film")
                .description("Third film description")
                .releaseDate(LocalDate.of(2020, 5, 10))
                .duration(333)
                .build();
        film3.setMpa(new Mpa(4, "R"));
        film3.setLikes(new HashSet<>());
        film3.setGenres(new HashSet<>(List.of(new Genre(2, "Драма"))));
    }

    @Test
    public void testCreateUserAndGetUserById() {
        user1 = userStorage.createUser(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user1.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", this.user1.getId())
                                .hasFieldOrPropertyWithValue("name", "First"));
    }

    @Test
    public void testGetUsers() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        List<User> listUsers = userStorage.getAllUsers();
        assertThat(listUsers).contains(user1);
        assertThat(listUsers).contains(user2);
    }

    @Test
    public void testUpdateUser() {
        user1 = userStorage.createUser(user1);
        User updateUser = User.builder()
                .id(user1.getId())
                .name("UpdateFirst")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();
        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.updateUser(updateUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "UpdateFirst")
                );
    }

    @Test
    public void testCreateFilmAndGetFilmById() {
        film1 = filmStorage.addFilm(film1);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film1.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", film1.getId())
                        .hasFieldOrPropertyWithValue("name", "First film")
                );
    }

    @Test
    public void testGetFilms() {
        film1 = filmStorage.addFilm(film1);
        film2 = filmStorage.addFilm(film2);
        film3 = filmStorage.addFilm(film3);
        List<Film> listFilms = filmStorage.getAllFilms();
        assertThat(listFilms).contains(film1);
        assertThat(listFilms).contains(film2);
        assertThat(listFilms).contains(film3);
    }

    @Test
    public void testUpdateFilm() {
        film1 = filmStorage.addFilm(film1);
        Film updateFilm = Film.builder()
                .id(film1.getId())
                .name("UpdateName")
                .description("UpdateDescription")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(122)
                .build();
        updateFilm.setMpa(new Mpa(1, "G"));
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.updateFilm(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "UpdateName")
                                .hasFieldOrPropertyWithValue("description", "UpdateDescription")
                );
    }

    @Test
    public void testAddLike() {
        user1 = userStorage.createUser(user1);
        film1 = filmStorage.addFilm(film1);
        filmService.addLike(film1.getId(), user1.getId());
        film1 = filmStorage.getFilmById(film1.getId());
        assertThat(film1.getLikes()).hasSize(1);
        assertThat(film1.getLikes()).contains(user1.getId());
    }

    @Test
    public void testDeleteLike() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        film1 = filmStorage.addFilm(film1);
        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user2.getId());
        filmService.deleteLike(film1.getId(), user1.getId());
        film1 = filmStorage.getFilmById(film1.getId());
        assertThat(film1.getLikes()).hasSize(1);
        assertThat(film1.getLikes()).contains(user2.getId());
    }

    @Test
    public void testGetPopularFilms() {

        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        user3 = userStorage.createUser(user3);

        film1 = filmStorage.addFilm(film1);
        filmService.addLike(film1.getId(), user1.getId());

        film2 = filmStorage.addFilm(film2);
        filmService.addLike(film2.getId(), user1.getId());
        filmService.addLike(film2.getId(), user2.getId());
        filmService.addLike(film2.getId(), user3.getId());

        film3 = filmStorage.addFilm(film3);
        filmService.addLike(film3.getId(), user1.getId());
        filmService.addLike(film3.getId(), user2.getId());

        List<Film> listFilms = filmService.getTopCountFilms(5);

        assertThat(listFilms).hasSize(3);

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Second film"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Third film"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "First film"));
    }

    @Test
    public void testAddFriend() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userService.addFriend(user1.getId(), user2.getId());
        assertThat(userService.getAllFriends(user1.getId())).hasSize(1);
        assertThat(userService.getAllFriends(user1.getId())).contains(user2);
    }

    @Test
    public void testDeleteFriend() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        user3 = userStorage.createUser(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.deleteFriend(user1.getId(), user2.getId());
        assertThat(userService.getAllFriends(user1.getId())).hasSize(1);
        assertThat(userService.getAllFriends(user1.getId())).contains(user3);
    }

    @Test
    public void testGetFriends() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        user3 = userStorage.createUser(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        assertThat(userService.getAllFriends(user1.getId())).hasSize(2);
        assertThat(userService.getAllFriends(user1.getId())).contains(user2, user3);
    }

    @Test
    public void testGetCommonFriends() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        user3 = userStorage.createUser(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user1.getId());
        userService.addFriend(user2.getId(), user3.getId());
        assertThat(userService.getCommonFriends(user1.getId(), user2.getId())).hasSize(1);
        assertThat(userService.getCommonFriends(user1.getId(), user2.getId()))
                .contains(user3);
    }
}

