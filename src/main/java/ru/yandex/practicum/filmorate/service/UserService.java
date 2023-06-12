package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(@Valid User user) {
        validateUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(@Validated(Update.class) User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long id, Long friendId) {
        getUserById(friendId);
        getUserById(id).addFriend(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        getUserById(friendId);
        getUserById(id).deleteFriend(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(Long id) {
        getUserById(id);
        return userStorage.getAllFriends(id);

    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        Set<User> commonFriends = new HashSet<>(userStorage.getAllFriends(id));
        commonFriends.retainAll(userStorage.getAllFriends(otherId));

        for (Long friendId : user.getFriends()) {
            if (otherUser.getFriends().contains(friendId)) {
                commonFriends.add(userStorage.getUserById(friendId));
            }
        }

        return new ArrayList<User>(commonFriends);
    }


    private void validateUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.error("Ошибки в e-mail пользователя");
            throw new ValidationException("Неправильный e-mail пользователя");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Неправильный логин пользователя");
            throw new ValidationException("Неправильный логин пользователя");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неправильная дата рождения пользователя");
            throw new ValidationException("Неправильная дата рождения пользователя");
        }
    }
}
