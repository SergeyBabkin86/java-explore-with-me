package ru.practicum.explore.service.event;

import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.event.dto.EventShortDto;
import ru.practicum.explore.model.event.dto.NewEventDto;
import ru.practicum.explore.model.event.dto.UpdateEventRequest;

import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> findAllCreatedByUser(Long userId, int from, int size);

    EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByIdAndInitiatorId(Long userId, Long eventId);

    EventFullDto cancelByInitiator(Long userId, Long eventId);

    EventFullDto publish(Long eventId);
}
