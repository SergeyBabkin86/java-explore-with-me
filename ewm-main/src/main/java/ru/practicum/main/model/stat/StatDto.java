package ru.practicum.main.model.stat;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class StatDto {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
