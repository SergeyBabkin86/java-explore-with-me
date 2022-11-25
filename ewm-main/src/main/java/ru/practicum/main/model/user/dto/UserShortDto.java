package ru.practicum.main.model.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserShortDto {

    Long id;

    String name;
}
