package ru.practicum.explore.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.mapper.ParticipationRequestMapper;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.QEvent;
import ru.practicum.explore.model.event.dto.*;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.model.request.dto.ParticipationRequestDto;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.ParticipationRequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explore.mapper.EventMapper.toEventFullDto;
import static ru.practicum.explore.mapper.EventMapper.toEventShortDto;
import static ru.practicum.explore.mapper.ParticipationRequestMapper.toParticipationRequestDto;
import static ru.practicum.explore.utilities.Checker.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final ParticipationRequestRepository requestRepository;

    // FIXME "PUBLIC EVENTS"
    @Override
    public Collection<EventShortDto> findAllByFilter(GetEventRequest request, int from, int size) {
        var event = QEvent.event;
        var conditions = new ArrayList<BooleanExpression>();
        var sort = makeSortOrder(request.getSort());
        var pageRequest = PageRequest.of(from / size, size, sort);

        conditions.add(event.state.eq(EventState.PUBLISHED));

        if (request.getText() != null) {
            conditions.add(event.annotation.containsIgnoreCase(request.getText())
                    .or(event.description.containsIgnoreCase(request.getText())));
        }

        if (request.getCategories() != null) {
            conditions.add(event.category.id.in(request.getCategories()));
        }

        if (request.getPaid() != null) {
            if (request.getPaid()) {
                conditions.add(event.paid.eq(true));
            } else {
                conditions.add(event.paid.eq(false));
            }
        }

        if (request.getRangeStart() != null) {
            conditions.add(event.eventDate.after(request.getRangeStart()));
        }

        if (request.getRangeEnd() != null) {
            conditions.add(event.eventDate.before(request.getRangeEnd()));
        }

        if (request.getRangeStart() == null && request.getRangeEnd() == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }

        if (request.getOnlyAvailable()) {
            conditions.add(event.confirmedRequests.ne(event.participantLimit));
        }

        var finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        var events = eventRepository.findAll(finalCondition, pageRequest);

        return toEventShortDto(events);

        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        //       нужно сохранить в сервисе статистики
    }

    @Override
    public EventFullDto findById(Long eventId) {
        var event = checkEventExists(eventId, eventRepository);
        if (event.getState().equals(EventState.PUBLISHED)) {
            return toEventFullDto(event);
        } else {
            throw new RuntimeException(format("Event with id=%s is not published and can not be shown.",
                    eventId));
            //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
            //       нужно сохранить в сервисе статистики
        }
    }

    //FIXME: "PRIVATE EVENTS"
    @Override
    public Collection<EventShortDto> findAllCreatedByUser(Long userId, int from, int size) {
        var pageRequest = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, pageRequest).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest) {

        var eventId = updateEventRequest.getEventId();
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        var eventDate = updateEventRequest.getEventDate();

        if (event == null) {
            throw new EntityNotFoundException(format("Event with id=%s created by user with id=%s was not found.",
                    eventId,
                    userId));
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RuntimeException(format("Event with id=%s is already published and can not been updated.",
                    eventId));
        }

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(checkCategoryExists(updateEventRequest.getCategory(), categoryRepository));
        }

        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }

        if (eventDate != null) {
            checkEventDateTime(eventDate);
            event.setEventDate(eventDate);
        }

        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }

        event.setState(EventState.PENDING);

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDateTime(newEventDto.getEventDate());

        var category = checkCategoryExists(newEventDto.getCategory(), categoryRepository);
        var user = checkUserExists(userId, userRepository);
        var event = eventRepository.save(EventMapper.toEvent(newEventDto, category, user));

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto findByIdAndInitiatorId(Long userId, Long eventId) {
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new EntityNotFoundException(format("Event with id=%s was not found.", eventId));
        }

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelByInitiator(Long userId, Long eventId) {
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new EntityNotFoundException(format("Event with id=%s was not found.", eventId));
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new RuntimeException(format("Only events with state=%s can be cancelled.", EventState.PENDING));
        }

        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    //TODO: TO PRIVATE SERVICE
    @Override
    public Collection<ParticipationRequestDto> findAllByEventId(Long userId, Long eventId) {
        checkUserExists(userId, userRepository);
        checkEventExists(eventId, eventRepository);

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new RuntimeException(format("Event with id=%s was not created by user with id=%s. " +
                    "It is impossible to get the participation requests in someone else's event.", eventId, userId));
        }
        var request = requestRepository.findAllByEventId(eventId);
        return request.stream().map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        checkUserExists(userId, userRepository);
        var event = checkEventExists(eventId, eventRepository);
        var request = checkPartRequestExists(requestId, requestRepository);

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new RuntimeException(format("Event with id=%s was not created by user with id=%s. " +
                    "It is impossible to confirm the participation request in someone else's event.", eventId, userId));
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new RuntimeException("Participant limit is unlimited or has been already achieved. " +
                    "It is impossible to take a part in the event.");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            for (ParticipationRequest pr : requestRepository.findAllByEventId(eventId)) {
                pr.setStatus(RequestStatus.REJECTED);
                requestRepository.save(pr);
            }
        }

        return toParticipationRequestDto(request);
    }

    //TODO: TO PRIVATE SERVICE
    @Override
    public ParticipationRequestDto rejectParticipationRequest(Long requesterId, Long eventId, Long requestId) {
        checkUserExists(requesterId, userRepository);
        checkEventExists(eventId, eventRepository);

        var request = checkPartRequestExists(requestId, requestRepository);

        request.setStatus(RequestStatus.REJECTED);

        return toParticipationRequestDto(request);
    }


    //FIXME: "ADMIN EVENTS"

    @Override
    public Collection<EventFullDto> findAllByAdmin(GetEventRequest request, int from, int size) {
        var event = QEvent.event;
        var conditions = new ArrayList<BooleanExpression>();
        var pageRequest = PageRequest.of(from / size, size);

        if (request.getUsers() != null) {
            conditions.add(event.initiator.id.in(request.getUsers()));
        }

        if (request.getStates() != null) {
            System.out.println(Arrays.toString(request.getStates()));
            conditions.add(event.state.stringValue().in(request.getStates()));
        }

        if (request.getCategories() != null) {
            conditions.add(event.category.id.in(request.getCategories()));
        }

        if (request.getRangeStart() != null) {
            conditions.add(event.eventDate.after(request.getRangeStart()));
        }

        if (request.getRangeEnd() != null) {
            conditions.add(event.eventDate.before(request.getRangeEnd()));
        }

        if (request.getRangeStart() == null && request.getRangeEnd() == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }

        var finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        var events = eventRepository.findAll(finalCondition, pageRequest);

        return toEventFullDto(events);
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest request) {
        var event = checkEventExists(eventId, eventRepository);

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            event.setCategory(checkCategoryExists(request.getCategory(), categoryRepository));
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getLocation() != null) {
            if (request.getLocation().getLat() != null) {
                event.setLat(request.getLocation().getLat());
            }

            if (request.getLocation().getLon() != null) {
                event.setLon(request.getLocation().getLon());
            }
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto publish(Long eventId) {
        var event = checkEventExists(eventId, eventRepository);
        var publishedOn = LocalDateTime.now();

        if (!event.getState().equals(EventState.PENDING)) {
            throw new RuntimeException(format("Only events with state=%s can be published. Current event state is %s",
                    EventState.PENDING,
                    event.getState()));
        }

        if (publishedOn.isBefore(LocalDateTime.now().minusHours(1))) {
            throw new RuntimeException("The start date of the event must be no earlier " +
                    "than one hour from the date of publication.");
        }
        event.setPublishedOn(publishedOn);
        event.setState(EventState.PUBLISHED);

        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto reject(Long eventId) {
        var event = checkEventExists(eventId, eventRepository);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RuntimeException(format("Event with id=%s was %S. Forbidden to reject.",
                    eventId,
                    event.getState()));
        }
        event.setState(EventState.CANCELED);
        return toEventFullDto(eventRepository.save(event));
    }

    private void checkEventDateTime(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RuntimeException("The time for which the event is scheduled " +
                    "cannot be earlier than two hours from the current moment.");
        }
    }

    private Sort makeSortOrder(GetEventRequest.Sort sort) {
        switch (sort) {
            case VIEWS:
                return Sort.by("views").descending();
            case EVENT_DATE:
                return Sort.by("eventDate").descending();
            default:
                throw new EntityNotFoundException("Unknown sort order.");
        }
    }


}
