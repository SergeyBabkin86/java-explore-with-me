package ru.practicum.main.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.main.error.exceptions.EntityNotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.model.event.EventState;
import ru.practicum.main.model.event.QEvent;
import ru.practicum.main.model.event.dto.*;
import ru.practicum.main.model.stat.StatDto;
import ru.practicum.main.model.stat.ViewStatDto;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.utilities.GetEventRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.main.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main.mapper.EventMapper.toEventShortDto;
import static ru.practicum.main.utilities.Checker.*;
import static ru.practicum.main.utilities.Constants.dateTimeFormat;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;


    @Override
    public Collection<EventShortDto> getEvents(GetEventRequest eventRequest,
                                               PageRequest pageRequest,
                                               HttpServletRequest servletRequest) {

        var finalCondition = getFinalConditions(eventRequest);
        pageRequest.withSort(makeSortOrder(eventRequest.getSort()));

        addStat(servletRequest);

        return toEventShortDto(eventRepository.findAll(finalCondition, pageRequest));
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest servletRequest) {
        var event = checkEventExists(eventId, eventRepository);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RuntimeException(format("Event with id=%s is not published and can not be shown.", eventId));
        }
        addStat(servletRequest);
        event.setViews(getViewsFromStat(servletRequest, event.getCreatedOn(), event.getViews()));

        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventShortDto> findAllCreatedByUser(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByUser(Long userId, UpdateEventRequest updateEventRequest) {
        var eventId = updateEventRequest.getEventId();
        var event = eventRepository.findByInitiatorIdAndId(userId, eventId);
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

        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(event::setTitle);

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(checkCategoryExists(updateEventRequest.getCategory(), categoryRepository));
        }

        if (updateEventRequest.getEventDate() != null) {
            checkEventDateTime(eventDate, Duration.ofHours(2));
            event.setEventDate(eventDate);
        }

        event.setState(EventState.PENDING);

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDateTime(newEventDto.getEventDate(), Duration.ofHours(2));

        var user = checkUserExists(userId, userRepository);
        var category = checkCategoryExists(newEventDto.getCategory(), categoryRepository);
        var event = eventRepository.save(EventMapper.toEvent(newEventDto, category, user));

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto findByInitiatorIdAndEventId(Long userId, Long eventId) {
        var event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new EntityNotFoundException(format("Event with id=%s was not found.", eventId));
        }
        return toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelByInitiator(Long userId, Long eventId) {
        var event = checkEventExists(eventId, eventRepository);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new RuntimeException(format("Only initiator with id=%s can cancel the event. Got user id=%s",
                    eventId,
                    userId));
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new RuntimeException(format("Only events with state=%s can be cancelled.", EventState.PENDING));
        }
        event.setState(EventState.CANCELED);

        return toEventFullDto(event);
    }

    @Override
    public Collection<EventFullDto> findAllByAdmin(GetEventRequest request, PageRequest pageRequest) {
        return toEventFullDto(eventRepository.findAll(getFinalConditions(request), pageRequest));
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest updateEventRequest) {
        var event = checkEventExists(eventId, eventRepository);

        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(event::setTitle);

        Optional.ofNullable(updateEventRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(checkCategoryExists(updateEventRequest.getCategory(), categoryRepository));
        }
        if (updateEventRequest.getEventDate() != null) {
            checkEventDateTime(updateEventRequest.getEventDate(), Duration.ofHours(2));
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            if (updateEventRequest.getLocation().getLat() != null) {
                event.setLat(updateEventRequest.getLocation().getLat());
            }
            if (updateEventRequest.getLocation().getLon() != null) {
                event.setLon(updateEventRequest.getLocation().getLon());
            }
        }

        return toEventFullDto(event);
    }

    @Override
    public EventFullDto publish(Long eventId) {
        var event = checkEventExists(eventId, eventRepository);

        if (!event.getState().equals(EventState.PENDING)) {
            throw new RuntimeException(format("Only events with state=%s can be published. Current event state is %s",
                    EventState.PENDING,
                    event.getState()));
        }
        if (LocalDateTime.now().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new RuntimeException("The start date of the event must be no earlier " +
                    "than one hour from the date of publication.");
        }
        event.setPublishedOn(LocalDateTime.now());
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

    private void checkEventDateTime(LocalDateTime localDateTime, Duration duration) {
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(duration.toHours()))) {
            throw new RuntimeException(format("The time for which the event is scheduled " +
                    "cannot be earlier than %s hours from the current moment.", duration.toHours()));
        }
    }

    private BooleanExpression getFinalConditions(GetEventRequest eventRequest) {
        var event = QEvent.event;
        var conditions = new ArrayList<BooleanExpression>();

        if (eventRequest.getText() != null) {
            conditions.add(event.annotation.containsIgnoreCase(eventRequest.getText())
                    .or(event.description.containsIgnoreCase(eventRequest.getText())));
        }
        if (eventRequest.getUsers() != null) {
            conditions.add(event.initiator.id.in(eventRequest.getUsers()));
        }
        if (eventRequest.getStates() != null) {
            conditions.add(event.state.eq(EventState.PUBLISHED));
        }
        if (eventRequest.getCategories() != null) {
            conditions.add(event.category.id.in(eventRequest.getCategories()));
        }
        if (eventRequest.getPaid() != null) {
            if (eventRequest.getPaid()) {
                conditions.add(event.paid.eq(true));
            } else {
                conditions.add(event.paid.eq(false));
            }
        }
        if (eventRequest.getRangeStart() == null && eventRequest.getRangeEnd() == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        } else {
            if (eventRequest.getRangeStart() != null) {
                conditions.add(event.eventDate.after(eventRequest.getRangeStart()));
            }
            if (eventRequest.getRangeEnd() != null) {
                conditions.add(event.eventDate.before(eventRequest.getRangeEnd()));
            }
        }
        if (eventRequest.getOnlyAvailable() != null) {
            if (eventRequest.getOnlyAvailable()) {
                conditions.add(event.confirmedRequests.ne(event.participantLimit));
            }
        }
        if (conditions.isEmpty()) {
            conditions.add(event.id.isNotNull());
        }

        return conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
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

    private void addStat(HttpServletRequest servletRequest) {
        var statDto = StatDto.builder()
                .app("ewm-main-service")
                .ip(servletRequest.getRemoteAddr())
                .uri(servletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        webClient.post()
                .uri("/hit")
                .body(Mono.just(statDto), StatDto.class)
                .exchangeToMono(rs -> Mono.just(rs.mutate()))
                .block();
    }

    private Integer getViewsFromStat(HttpServletRequest servletRequest, LocalDateTime createdOn, Integer views) {
        var response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", createdOn.format(dateTimeFormat()))
                        .queryParam("end", LocalDateTime.now().format(dateTimeFormat()))
                        .queryParam("uris", servletRequest.getRequestURI())
                        .queryParam("unique", true)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStatDto.class)
                .collectList()
                .block();

        Integer hits = views;
        if (response != null && !response.isEmpty()) {
            for (ViewStatDto vsd : response) {
                if (vsd.getHits() != null) {
                    hits = vsd.getHits();
                }
            }
        }
        return hits;
    }
}
