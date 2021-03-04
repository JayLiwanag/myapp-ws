package org.myapp.dev.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler {

    public ErrorDetails getErrorDetails(String msg, String errorMessage, WebRequest req) {
        return new ErrorDetails(LocalDateTime.now(),
                msg,
                errorMessage,
                ((ServletWebRequest)req).getRequest().getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest req){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = new String(String.format("Validation failed for [%s] argument.", ex.getBindingResult().getAllErrors().size()));
        String error = ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));

        ErrorDetails errorDetails = getErrorDetails(message, error, req);

        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorDetails> userServiceExceptionHandler(UserServiceException ex, WebRequest req){
        ErrorDetails errorDetails = getErrorDetails(ex.getMessage(), ex.getErrorMessage(), req);


        return ResponseEntity.status(ex.getStatus()).body(errorDetails);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundExceptionHandler(NotFoundException ex, WebRequest req){
        ErrorDetails errorDetails = getErrorDetails(ex.getMessage(), ex.getErrorMessage(), req);

        return ResponseEntity.status(ex.getStatus()).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest req){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorDetails errorDetails = getErrorDetails(ex.getMessage(), status.name(), req);

        return ResponseEntity.status(status).body(errorDetails);
    }
}
