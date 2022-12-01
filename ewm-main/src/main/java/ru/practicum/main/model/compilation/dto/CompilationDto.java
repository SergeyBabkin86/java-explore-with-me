package ru.practicum.main.model.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.main.model.event.dto.EventShortDto;

import java.util.Collection;

@Value
@Builder
public class CompilationDto {

    Long id;
    String title;
    Boolean pinned;
    Collection<EventShortDto> events;
}

