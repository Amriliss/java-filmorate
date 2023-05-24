package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(id);
    }

    public List<User> getAllFriends(Long id) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : userStorage.getUserById(id).getFriends()) {
            friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        for (Long friendId : user.getFriends()) {
            if (otherUser.getFriends().contains(friendId)) {
                commonFriends.add(userStorage.getUserById(friendId));
            }
        }

        return commonFriends;
    }
}
