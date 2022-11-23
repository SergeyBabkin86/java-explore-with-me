package ru.practicum.explore.model.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.explore.model.event.dto.EventShortDto;

import java.util.Collection;

@Value
@Builder
public class CompilationDto {

    Long id;
    String title;
    Boolean pinned;
    Collection<EventShortDto> events;
}

