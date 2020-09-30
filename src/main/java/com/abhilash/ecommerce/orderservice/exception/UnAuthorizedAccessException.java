package com.abhilash.ecommerce.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class represents a custom exception for unauthorized resource access from the user
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedAccessException extends RuntimeException {
    public UnAuthorizedAccessException() {
        super();
    }

    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}
