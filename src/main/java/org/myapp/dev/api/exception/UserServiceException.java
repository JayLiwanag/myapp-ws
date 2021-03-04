package org.myapp.dev.api.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String errorMessage = "Bad Request.";

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UserServiceException(String message, String errorMessage, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
