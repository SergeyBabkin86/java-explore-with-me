package ru.practicum.main.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main.model.category.Category;
import ru.practicum.main.model.event.EventState;
import ru.practicum.main.model.location.dto.LocationShortDto;
import ru.practicum.main.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
public class EventFullDto {

    Long id;

    String annotation;

    Category category;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationShortDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;

    Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    UserShortDto initiator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    EventState state;

    Integer views;
}
