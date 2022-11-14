package ru.practicum.explore.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.Location;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;

@Value
public class EventFullDto {

    Long id;

    String annotation;

    Category category;

    String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;

    Integer confirmedRequests;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    User initiator;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    EventState state;

    Integer views;
}
