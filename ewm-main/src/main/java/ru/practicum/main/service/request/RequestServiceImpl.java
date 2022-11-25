package ru.practicum.main.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.exceptions.EntityNotFoundException;
import ru.practicum.main.error.exceptions.ValidationException;
import ru.practicum.main.mapper.RequestMapper;
import ru.practicum.main.model.event.Event;
import ru.practicum.main.model.event.EventState;
import ru.practicum.main.model.request.RequestStatus;
import ru.practicum.main.model.request.dto.ParticipationRequestDto;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.main.mapper.RequestMapper.toRequestDto;
import static ru.practicum.main.utilities.Checker.*;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ParticipationRequestDto> findAllByRequesterId(Long requesterId) {
        return requestRepository
                .findAllByRequesterId(requesterId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ParticipationRequestDto> findAllByEventId(Long userId, Long eventId) {
        checkUserExists(userId, userRepository);
        checkEventExists(eventId, eventRepository);

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new RuntimeException(format("Event with id=%s was not created by user with id=%s. " +
                    "It is impossible to get the participation requests in someone else's event.", eventId, userId));
        }
        var request = requestRepository.findAllByEventId(eventId);
        return request.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto save(Long requesterId, Long eventId) {
        if (eventId == null) {
            throw new ValidationException("EventId shouldn't be null.");
        }
        var requester = checkUserExists(requesterId, userRepository);
        var event = checkEventExists(eventId, eventRepository);

        validateRequest(requesterId, event);

        var request = RequestMapper.toRequest(event, requester);

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto confirm(Long userId, Long eventId, Long requestId) {
        checkUserExists(userId, userRepository);
        var event = checkEventExists(eventId, eventRepository);
        var request = checkRequestExists(requestId, requestRepository);

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new EntityNotFoundException(format("Event with id=%s was not created by user with id=%s. " +
                    "It is impossible to confirm the participation request in someone else's event.", eventId, userId));
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ValidationException("Participant limit is unlimited or has been already achieved. " +
                    "It is impossible to take a part in the event.");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            requestRepository.findAllByEventId(eventId).forEach(pr -> {
                pr.setStatus(RequestStatus.REJECTED);
                requestRepository.save(pr);
            });
        }

        return toRequestDto(request);
    }

    @Override
    public ParticipationRequestDto reject(Long requesterId, Long eventId, Long requestId) {
        checkUserExists(requesterId, userRepository);
        checkEventExists(eventId, eventRepository);

        var request = checkRequestExists(requestId, requestRepository);

        request.setStatus(RequestStatus.REJECTED);

        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancel(Long requesterId, Long requestId) {

        var request = checkRequestExists(requestId, requestRepository);
        var event = checkEventExists(request.getEvent().getId(), eventRepository);

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);

        requestRepository.save(request);

        return toRequestDto(request);
    }

    private void validateRequest(Long requesterId, Event event) {
        if (requestRepository.countAllByRequesterIdAndEventId(requesterId, event.getId()) != 0) {
            throw new ValidationException("Repeat participation request is prohibited.");
        }

        if (event.getInitiator().getId().equals(requesterId)) {
            throw new ValidationException("It is impossible to take a part in own-created event.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("It is impossible to take a part in unpublished event.");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ValidationException("Participant limit is achieved. It is impossible to take a part in the event.");
        }
    }
}
