package ru.practicum.explore.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final MethodArgumentNotValidException e) {
        log.debug("400 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(getFormattedTimestamp())
                .build();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final ConstraintViolationException e) {
        log.debug("400 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason(new ArrayList<>(e.getConstraintViolations()).get(0).getMessageTemplate())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(getFormattedTimestamp())
                .build();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleThrowable(final EntityNotFoundException e) {
        log.debug("404 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(getFormattedTimestamp())
                .build();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleThrowable(final DataIntegrityViolationException e) {
        log.debug("409 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason(e.getCause().getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(getFormattedTimestamp())
                .build();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleThrowable(final org.hibernate.exception.ConstraintViolationException e) {
        log.debug("409 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason(e.getCause().getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(getFormattedTimestamp())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse defaultHandle(final Exception e) {
        log.debug("500 {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .errors(getStackTraceStrings(e))
                .message(e.getMessage())
                .reason("Error occurred.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    private List<String> getStackTraceStrings(Exception e) {
        return Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }

    private String getFormattedTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}