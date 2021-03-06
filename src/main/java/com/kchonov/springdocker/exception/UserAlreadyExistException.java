package com.kchonov.springdocker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Krasi
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super();
    }
    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyExistException(String message) {
        super(message);
    }
    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
