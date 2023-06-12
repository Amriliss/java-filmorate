package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static long id = 0;

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
//            count++;
            return user;
        } else {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        if (users.containsKey(user.getId())) {
            throw new AlreadyExistException("Пользователь уже существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);

        } else {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(id);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : getUserById(id).getFriends()) {
            friends.add(getUserById(friendId));
        }
        return friends;
    }
}
