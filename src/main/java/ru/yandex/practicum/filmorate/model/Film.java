package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    private Set<Long> likes = new HashSet<>();

    @NotBlank(message = "Поле 'название или описание' не должно быть пустым")
    private String name;
    private String description;

    @NotNull(message = "Поле 'дата выпуска или продолжительность' не должно быть пустым")
    private String releaseDate;
    private Integer duration;

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void deleteLike(Long userId) {
        likes.remove(userId);
    }

    public static class Compare implements Comparator<Film> {
        @Override
        public int compare(Film f1, Film f2) {
            return f2.getLikes().size() - f1.getLikes().size();
        }
    }
}