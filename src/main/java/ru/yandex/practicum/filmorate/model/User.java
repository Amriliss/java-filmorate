package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
public class User {
    private long id;
    @JsonIgnore
    private final Set<Long> friends = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}