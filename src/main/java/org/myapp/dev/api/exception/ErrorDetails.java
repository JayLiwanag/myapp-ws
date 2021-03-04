package org.myapp.dev.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String error;
    private String path;

//    public ErrorDetails(LocalDateTime timestamp, int status, String message, String error, String path) {
//        this.timestamp = timestamp;
//        this.status = status;
//        this.message = message;
//        this.error = error;
//        this.path = path;
//    }
}
