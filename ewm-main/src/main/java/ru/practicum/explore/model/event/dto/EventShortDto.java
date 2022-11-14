package ru.practicum.explore.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;

@Value
public class EventShortDto {

    Long id;

    String annotation;

    Category category;

    Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    User initiator;

    Boolean paid;

    String title;

    Integer views;
}
