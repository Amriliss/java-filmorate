package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getMpas();

    Mpa getMpaById(Integer id) throws DataNotFoundException;
}
