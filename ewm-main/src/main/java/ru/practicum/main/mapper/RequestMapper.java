package ru.practicum.main.mapper;

import ru.practicum.main.model.event.Event;
import ru.practicum.main.model.request.ParticipationRequest;
import ru.practicum.main.model.request.RequestStatus;
import ru.practicum.main.model.request.dto.ParticipationRequestDto;
import ru.practicum.main.model.user.User;

import java.time.LocalDateTime;

public class RequestMapper {

    public static ParticipationRequest toRequest(Event event, User requester) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();
    }

    public static ParticipationRequestDto toRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }
}
