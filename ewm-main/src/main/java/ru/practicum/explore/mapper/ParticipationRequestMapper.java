package ru.practicum.explore.mapper;

import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.model.request.dto.ParticipationRequestDto;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipationRequest (ParticipationRequestDto participationRequestDto,
                                                               Event event,
                                                               User requester) {
        return ParticipationRequest.builder()
                .id(null)
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto (ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(participationRequest.getId(),
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus());
    }
}
