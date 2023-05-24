package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private long id;

    @Email(message = "Введите правильный email")
    private String email;

    @NotBlank(message = "Поле 'Логин и Имя' не должно быть пустым")
    private String login;
    private String name;

    @PastOrPresent(message = "Поле 'Дата рождения' заполнено неправильно")
    private LocalDate birthday;
}