# filmorate - приложение для поиска фильмов по рейтингу

---
### схема базы данных:

![схема базы данных:](./img/DbDiagram.png)

---
### Основные SQL запросы к базе данных:
```
--Получение списка всех пользователей
SELECT *
FROM users;

-- Выбрать всех друзей пользователя 1
SELECT u.id,
u.name
FROM users u
LEFT JOIN friends f on u.id = f.friend_id
WHERE f.user_id = 1;

-- Выбрать все фильмы
SELECT *
FROM film;

-- Все фильмы с рейтингами
SELECT f.name,
f.description,
f.duration,
r.name raiting
FROM film f
LEFT JOIN rating r on f.rating_id = r.id;

-- Посмотреть все жанры
SELECT *
FROM genre;

-- Выбрать фильмы с отображением рейтингов и жанров
SELECT f.name,
f.description,
f.duration,
r.name raiting,
g.name genres
FROM film f
LEFT JOIN rating r on f.rating_id = r.id
LEFT JOIN film_genre fg on f.id = fg.film_id
LEFT JOIN genre g on fg.genre_id = g.id;

-- Получение ТОП-10 фильмов
SELECT f.name,
count(l.film_id) rate
FROM film f
JOIN likes l on f.id = l.film_id
GROUP BY f.name
ORDER BY rate desc
LIMIT 10
```