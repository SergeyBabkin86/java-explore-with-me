package ru.practicum.main.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.exceptions.ConflictException;
import ru.practicum.main.error.exceptions.EntityNotFoundException;
import ru.practicum.main.error.exceptions.ValidationException;
import ru.practicum.main.error.model.ErrorResponse;
import ru.practicum.main.error.model.ErrorState;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.utilities.Constants.dateTimeFormat;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final MethodArgumentNotValidException e) {
        log.debug("400 {}", e.getMessage(), e);
        return getErrorResponse(e, "Incorrect request.", ErrorState.BAD_REQUEST);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final ConstraintViolationException e) {
        log.debug("400 {}", e.getMessage(), e);
        return getErrorResponse(e, "Incorrect request.", ErrorState.BAD_REQUEST);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.debug("400 {}", e.getMessage(), e);
        return getErrorResponse(e, "Incorrect request.", ErrorState.BAD_REQUEST);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.debug("404 {}", e.getMessage(), e);
        return getErrorResponse(e, "Entity was not found.", ErrorState.NOT_FOUND);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.debug("409 {}", e.getMessage(), e);
        return getErrorResponse(e, "Conflict occurred.", ErrorState.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse defaultHandle(final Exception e) {
        log.debug("500 {}", e.getMessage(), e);

        return getErrorResponse(e, "Error occurred.", ErrorState.FORBIDDEN);
    }

    private ErrorResponse getErrorResponse(Exception e, String reason, ErrorState errorState) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reason)
                .status(errorState)
                .timestamp(LocalDateTime.now().format(dateTimeFormat()))
                .errors(List.of(e.getMessage()))
                .build();
    }
}