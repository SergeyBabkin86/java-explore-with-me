package ru.practicum.main.service.event;

import ru.practicum.main.model.event.dto.*;
import ru.practicum.main.utilities.GetEventRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> getEvents(GetEventRequest request,
                                        int from,
                                        int size,
                                        HttpServletRequest servletRequest);

    EventFullDto getEventById(Long eventId, HttpServletRequest servletRequest);

    Collection<EventShortDto> findAllCreatedByUser(Long userId, int from, int size);

    EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByIdAndInitiatorId(Long userId, Long eventId);

    EventFullDto cancelByInitiator(Long userId, Long eventId);

    Collection<EventFullDto> findAllByAdmin(GetEventRequest request, int from, int size);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest request);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);
}
