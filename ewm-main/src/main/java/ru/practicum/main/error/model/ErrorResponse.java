package ru.practicum.main.error.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ErrorResponse {
    String message;
    String reason;
    ErrorState status;
    String timestamp;
    List<String> errors;
}