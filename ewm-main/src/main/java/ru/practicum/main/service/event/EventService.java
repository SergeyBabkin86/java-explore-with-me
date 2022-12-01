package ru.practicum.main.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main.model.event.dto.*;
import ru.practicum.main.utilities.GetEventRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> getEvents(GetEventRequest request,
                                        PageRequest pageRequest,
                                        HttpServletRequest servletRequest);

    EventFullDto getEventById(Long eventId, HttpServletRequest servletRequest);

    Collection<EventShortDto> findAllCreatedByUser(Long userId, PageRequest pageRequest);

    EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByInitiatorIdAndEventId(Long userId, Long eventId);

    EventFullDto cancelByInitiator(Long userId, Long eventId);

    Collection<EventFullDto> findAllByAdmin(GetEventRequest request, PageRequest pageRequest);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest request);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);
}
