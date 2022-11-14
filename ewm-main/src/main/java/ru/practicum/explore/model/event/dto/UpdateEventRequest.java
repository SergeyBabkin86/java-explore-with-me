package ru.practicum.explore.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class UpdateEventRequest {

    @NotNull(message = "Event id should not be null.")
    @Min(value = 1, message = "Event id should be equal or above 1.")
    Long eventId;

    @NotBlank(message = "Event annotation shouldn't be null or blank.")
    @Size(min = 20, max = 2000, message = "Event annotation size should be between 20 and 2000 characters.")
    String annotation;

    @Positive(message = "Category id shouldn be above zero.")
    Long category;

    @NotBlank(message = "Event description shouldn't be null or blank.")
    @Size(min = 20, max = 7000, message = "Event description size should be between 20 and 7000 characters.")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Event date should be in future.")
    LocalDateTime eventDate;

    @NotNull(message = "Paid status shouldn't be null.")
    Boolean paid;

    @PositiveOrZero(message = "Participant limit should be positive or zero.")
    Integer participantLimit;

    @NotBlank(message = "Event description shouldn't be null or blank.")
    @Size(min = 3, max = 120, message = "Event title size should be between 3 and 120 characters.")
    String title;
}
