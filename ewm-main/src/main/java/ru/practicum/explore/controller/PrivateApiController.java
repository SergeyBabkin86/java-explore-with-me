package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.event.dto.EventShortDto;
import ru.practicum.explore.model.event.dto.NewEventDto;
import ru.practicum.explore.model.event.dto.UpdateEventRequest;
import ru.practicum.explore.model.request.dto.ParticipationRequestDto;
import ru.practicum.explore.service.event.EventService;
import ru.practicum.explore.service.participation.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class PrivateApiController {

    public final EventService eventService;
    private final ParticipationRequestService participationRequestService;


    @PostMapping(value = "/{userId}/events")
    public EventFullDto save(@PathVariable @Positive long userId,
                             @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.save(userId, newEventDto);
    }

    @PatchMapping(value = "/{userId}/events")
    public EventFullDto updateByUser(@PathVariable @Positive long userId,
                                     @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateByUser(userId, updateEventRequest);
    }

    @GetMapping(value = "/{userId}/events")
    public Collection<EventShortDto> findAllCreatedByUser(@PathVariable @Positive long userId,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.findAllCreatedByUser(userId, from, size);
    }

    @GetMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto findByIdAndInitiatorId(@PathVariable @Positive long userId,
                                               @PathVariable @Positive long eventId) {
        return eventService.findByIdAndInitiatorId(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto cancelByInitiator(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long eventId) {
        return eventService.cancelByInitiator(userId, eventId);
    }

    // ЗАПРОСЫ НА УЧАСТИЕ

    @GetMapping(value = "/{userId}/requests")
    public Collection<ParticipationRequestDto> findByIdAndInitiatorId(@PathVariable @Positive long userId) {
        return participationRequestService.findAllByRequesterId(userId);
    }

    @PostMapping(value = "/{userId}/requests")
    public ParticipationRequestDto save(@PathVariable @Positive long userId,
                                        @RequestParam @Positive long eventId) {
        return participationRequestService.save(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Positive long userId,
                                                              @PathVariable @Positive long requestId) {
        return participationRequestService.cancel(userId, requestId);
    }


}
