package ru.practicum.main.model.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    Long id;

    String name;

    String email;
}
