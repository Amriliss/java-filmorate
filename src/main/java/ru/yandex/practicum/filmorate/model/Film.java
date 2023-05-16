package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Film {
    private long id;

    @NotBlank(message = "Поле 'название или описание' не должно быть пустым")
    private String name;
    private String description;

    @NotNull(message = "Поле 'дата выпуска или продолжительность' не должно быть пустым")
    private String releaseDate;
    private Integer duration;

}