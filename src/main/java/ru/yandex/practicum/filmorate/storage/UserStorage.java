package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getAllFriends(Long id);
}
