package ru.practicum.main.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.practicum.main.model.location.dto.LocationShortDto;

import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class AdminUpdateEventRequest {

    String annotation;

    Long category;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationShortDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;
}
