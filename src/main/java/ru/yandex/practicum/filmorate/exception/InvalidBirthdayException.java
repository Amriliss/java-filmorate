package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException(String message) {
        super(message);
        log.error(message);
    }
}
