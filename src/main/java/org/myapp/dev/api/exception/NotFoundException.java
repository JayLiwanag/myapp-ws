package org.myapp.dev.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException{

    private HttpStatus status = HttpStatus.NOT_FOUND;
    private String errorMessage = "Not Found.";

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public NotFoundException(String message, String errorMessage, HttpStatus status) {
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