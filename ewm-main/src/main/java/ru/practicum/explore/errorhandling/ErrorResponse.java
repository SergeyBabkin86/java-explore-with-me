package ru.practicum.explore.errorhandling;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.List;

@Value
@Builder
public class ErrorResponse {
    List<String> errors;
    String message;
    String reason;
    HttpStatus status;
    String timestamp;
}