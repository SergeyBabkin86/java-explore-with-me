package ru.practicum.explore.service.event;

import ru.practicum.explore.model.event.dto.*;
import ru.practicum.explore.model.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> findAllByFilter(GetEventRequest request, int from, int size);

    EventFullDto findById(Long eventId);

    Collection<EventShortDto> findAllCreatedByUser(Long userId, int from, int size);

    EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByIdAndInitiatorId(Long userId, Long eventId);

    EventFullDto cancelByInitiator(Long userId, Long eventId);

    //TODO GET "/users/{userId}/events/{eventId}/requests"
    Collection<ParticipationRequestDto> findAllByEventId(Long requesterId, Long eventId);

    //TODO PATCH "/users/{userId}/events/{eventId}/requests/{reqId}/confirm"
    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectParticipationRequest(Long requesterId, Long eventId, Long requestId);

    Collection<EventFullDto> findAllByAdmin(GetEventRequest request, int from, int size);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest request);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);
}
