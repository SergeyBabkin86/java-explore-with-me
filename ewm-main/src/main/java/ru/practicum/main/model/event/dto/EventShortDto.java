package ru.practicum.main.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main.model.category.Category;
import ru.practicum.main.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
public class EventShortDto {

    Long id;

    String annotation;

    Category category;

    Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    UserShortDto initiator;

    Boolean paid;

    String title;

    Integer views;
}
