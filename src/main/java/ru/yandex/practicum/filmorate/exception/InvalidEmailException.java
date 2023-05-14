package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
        log.error(message);
    }
}