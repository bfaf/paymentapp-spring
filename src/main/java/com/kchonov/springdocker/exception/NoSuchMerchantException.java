package com.kchonov.springdocker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Krasi
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchMerchantException extends RuntimeException {
    public NoSuchMerchantException() {
        super();
    }
    public NoSuchMerchantException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoSuchMerchantException(String message) {
        super(message);
    }
    public NoSuchMerchantException(Throwable cause) {
        super(cause);
    }
}
