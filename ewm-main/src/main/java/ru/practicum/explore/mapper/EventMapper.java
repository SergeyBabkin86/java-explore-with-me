package ru.practicum.explore.mapper;

import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.event.dto.EventShortDto;
import ru.practicum.explore.model.event.dto.NewEventDto;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, Category category, User user) {

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .initiator(user)
                .publishedOn(null)
                .state(EventState.PENDING)
                .views(0)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle(),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getInitiator(),
                event.getPublishedOn(),
                event.getState(),
                event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getInitiator(),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }


}

