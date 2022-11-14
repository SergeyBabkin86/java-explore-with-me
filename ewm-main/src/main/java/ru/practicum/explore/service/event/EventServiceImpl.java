package ru.practicum.explore.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.event.dto.EventShortDto;
import ru.practicum.explore.model.event.dto.NewEventDto;
import ru.practicum.explore.model.event.dto.UpdateEventRequest;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.LocationRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explore.mapper.EventMapper.toEventFullDto;
import static ru.practicum.explore.utilities.Checker.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

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
        locationRepository.save(newEventDto.getLocation());

        checkEventDateTime(newEventDto.getEventDate());

        var category = checkCategoryExists(newEventDto.getCategory(), categoryRepository);
        var user = checkUserExists(userId, userRepository);
        var event = eventRepository.save(EventMapper.toEvent(newEventDto, category, user));

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto findByIdAndInitiatorId(Long userId, Long eventId) {
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event ==null) {
            throw new EntityNotFoundException(format("Event with id=%s was not found.", eventId));
        }

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelByInitiator(Long userId, Long eventId) {
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event ==null) {
            throw new EntityNotFoundException(format("Event with id=%s was not found.", eventId));
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new RuntimeException(format("Only events with state=%s can be cancelled.", EventState.PENDING));
        }

        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    // ADMIN EVENTS
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

    private void checkEventDateTime(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RuntimeException("The time for which the event is scheduled " +
                    "cannot be earlier than two hours from the current moment.");
        }
    }






}
