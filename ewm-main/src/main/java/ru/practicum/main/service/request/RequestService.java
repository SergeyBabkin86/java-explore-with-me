package ru.practicum.main.service.request;

import ru.practicum.main.model.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {

    Collection<ParticipationRequestDto> findAllByRequesterId(Long requesterId);

    //TODO: TO PRIVATE SERVICE
    Collection<ParticipationRequestDto> findAllByEventId(Long userId, Long eventId);

    ParticipationRequestDto save(Long userId, Long eventId);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long requestId);

    //TODO: TO PRIVATE SERVICE
    ParticipationRequestDto reject(Long requesterId, Long eventId, Long requestId);

    ParticipationRequestDto cancel(Long requesterId, Long participationRequestId);
}
