package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private Set<Long> friends = new HashSet<>();

    @Email(message = "Введите правильный email")
    private String email;

    @NotBlank(message = "Поле 'Логин и Имя' не должно быть пустым")
    private String login;
    private String name;

    @PastOrPresent(message = "Поле 'Дата рождения' заполнено неправильно")
    private LocalDate birthday;

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        friends.remove(friendId);
    }
}