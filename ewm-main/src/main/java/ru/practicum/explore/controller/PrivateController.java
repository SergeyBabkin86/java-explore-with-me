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
public class PrivateController {

    public final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping(value = "/{userId}/events")
    public Collection<EventShortDto> findAllEvensCreatedByUser(@PathVariable @Positive long userId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.findAllCreatedByUser(userId, from, size);
    }

    @PatchMapping(value = "/{userId}/events")
    public EventFullDto updateEventByUser(@PathVariable @Positive long userId,
                                          @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateByUser(userId, updateEventRequest);
    }

    @PostMapping(value = "/{userId}/events")
    public EventFullDto saveEvent(@PathVariable @Positive long userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.save(userId, newEventDto);
    }

    @GetMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto findEvenByIdAddedByInitiator(@PathVariable @Positive long userId,
                                                     @PathVariable @Positive long eventId) {
        return eventService.findByIdAndInitiatorId(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto cancelEventByIdAddedByInitiator(@PathVariable @Positive long userId,
                                                        @PathVariable @Positive long eventId) {
        return eventService.cancelByInitiator(userId, eventId);
    }

    //TODO: не работает
    @GetMapping(value = "/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> findRequestByUserAndEventId(@PathVariable @Positive long userId,
                                                                           @PathVariable @Positive long eventId) {
        return eventService.findAllByEventId(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable @Positive long userId,
                                                  @PathVariable @Positive long eventId,
                                                  @PathVariable @Positive long reqId) {
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable @Positive long userId,
                                                 @PathVariable @Positive long eventId,
                                                 @PathVariable @Positive long reqId) {
        return eventService.rejectParticipationRequest(userId, eventId, reqId);
    }

    @GetMapping(value = "/{userId}/requests")
    public Collection<ParticipationRequestDto> findAllRequestsByRequester(@PathVariable @Positive long userId) {
        return participationRequestService.findAllByRequesterId(userId);
    }

    @PostMapping(value = "/{userId}/requests")
    public ParticipationRequestDto saveRequest(@PathVariable @Positive long userId,
                                               @RequestParam(required = false) @Positive Long eventId) {
        return participationRequestService.save(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnRequest(@PathVariable @Positive long userId,
                                                    @PathVariable @Positive long requestId) {
        return participationRequestService.cancel(userId, requestId);
    }
}
