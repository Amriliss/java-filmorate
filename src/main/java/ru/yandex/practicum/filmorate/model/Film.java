package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    private long id;
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    @NotBlank(message = "Поле 'название или описание' не должно быть пустым")
    private String name;
    private String description;

    @NotNull(message = "Поле 'дата выпуска или продолжительность' не должно быть пустым")
    private String releaseDate;
    @Min(1)
    private Integer duration;

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void deleteLike(Long userId) {
        likes.remove(userId);
    }

    public long getRate() {
        return likes.size();
    }

    public static class compare implements Comparator<Film> {
        @Override
        public int compare(Film f1, Film f2) {
            return f2.getLikes().size() - f1.getLikes().size();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}