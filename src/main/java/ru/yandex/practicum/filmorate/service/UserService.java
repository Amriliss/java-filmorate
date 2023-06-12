package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
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
}
