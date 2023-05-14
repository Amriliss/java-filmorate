package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
        log.error(message);
    }
}
