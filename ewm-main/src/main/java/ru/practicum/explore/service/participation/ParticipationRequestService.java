package ru.practicum.explore.service.participation;

import ru.practicum.explore.model.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface ParticipationRequestService {

    Collection<ParticipationRequestDto> findAllByRequesterId(Long requesterId);

    ParticipationRequestDto save(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long requesterId, Long participationRequestId);
}
