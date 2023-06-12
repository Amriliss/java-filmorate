package ru.yandex.practicum.filmorate.storage.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
@Qualifier("UserDBStorage")
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.info("Добавлен новый пользователь с ID={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(""
                        + "UPDATE users "
                        + "SET email=?, login=?, name=?, birthday=? "
                        + "WHERE id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        User result = getUserById(user.getId());
        log.info("Обновлён пользователь: {}", result);
        return result;
    }

    @Override
    public User getUserById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(format(""
                    + "SELECT id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE id=%d", id), this::userMapping);
            log.info("Возвращён пользователь: {}", user);
            return user;
        } catch (RuntimeException e) {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Возвращены все пользователи");
        return jdbcTemplate.query("select * from USERS", this::userMapping);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {


        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {


        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getAllFriends(Long userId) {
        String sql = "SELECT friend_id AS id, email, login, name, birthday FROM friends" +
                " INNER JOIN users ON friends.friend_id = users.id WHERE friends.user_id = ?";
        return jdbcTemplate.query(sql, this::userMapping, userId);
    }

    private User userMapping(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}