package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Film {
    private long id;
    @NotBlank(message = "Поле 'название или описание' не должно быть пустым")
    private String name;
    private String description;
    @NotNull(message = "Поле 'дата выпуска или продолжительность' не должно быть пустым")
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();
    private Mpa mpa = new Mpa();
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void deleteLike(Long userId) {
        likes.remove(userId);
    }

    public long getRate() {
        return likes.size();
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

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());
        return values;
    }
}