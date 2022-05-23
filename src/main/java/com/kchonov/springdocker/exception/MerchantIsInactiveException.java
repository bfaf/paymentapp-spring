package com.kchonov.springdocker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Krasi
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MerchantIsInactiveException extends RuntimeException {
    
    public MerchantIsInactiveException() {
        super();
    }

    public MerchantIsInactiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MerchantIsInactiveException(String message) {
        super(message);
    }

    public MerchantIsInactiveException(Throwable cause) {
        super(cause);
    }
}
