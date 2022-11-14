package ru.practicum.explore.service.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;
import ru.practicum.explore.mapper.ParticipationRequestMapper;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.model.request.dto.ParticipationRequestDto;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.ParticipationRequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explore.mapper.ParticipationRequestMapper.toParticipationRequestDto;
import static ru.practicum.explore.utilities.Checker.*;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ParticipationRequestDto> findAllByRequesterId(Long requesterId) {
        return participationRequestRepository
                .findAllByRequesterId(requesterId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto save(Long requesterId, Long eventId) {
        var requester = checkUserExists(requesterId, userRepository);
        var event = checkEventExists(eventId, eventRepository);

        if (participationRequestRepository.countAllByRequesterIdAndEventId(requesterId, eventId) != 0) {
            throw new RuntimeException("Repeat participation request is prohibited.");
        }

        if (event.getInitiator().getId().equals(requesterId)) {
            throw new RuntimeException("It is impossible to take a part in own-created event.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RuntimeException("It is impossible to take a part in unpublished event.");
        }

        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new RuntimeException("Participant limit is achieved. It is impossible to take a part in the event.");
        }

        var participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();

        if (!event.getRequestModeration()) {
            participationRequest.setStatus(RequestStatus.APPROVED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public ParticipationRequestDto cancel(Long requesterId, Long participationRequestId) {

        var participationRequest = participationRequestRepository.findByRequesterIdAndId(requesterId,
                participationRequestId);
        var event = checkEventExists(participationRequest.getEvent().getId(), eventRepository);

        if (participationRequestId == null) {
            throw new EntityNotFoundException(format("Participation request with id=%s was not found, or " +
                            "created by another requester (not requester with id=%s)",
                    participationRequestId,
                    requesterId));
        }
        participationRequest.setStatus(RequestStatus.CANCELED);

        if (participationRequest.getStatus().equals(RequestStatus.APPROVED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        participationRequestRepository.save(participationRequest);

        return toParticipationRequestDto(participationRequest);
    }
}
