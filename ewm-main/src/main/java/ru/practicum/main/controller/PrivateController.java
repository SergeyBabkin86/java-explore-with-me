package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.event.dto.EventFullDto;
import ru.practicum.main.model.event.dto.EventShortDto;
import ru.practicum.main.model.event.dto.NewEventDto;
import ru.practicum.main.model.event.dto.UpdateEventRequest;
import ru.practicum.main.model.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.event.EventService;
import ru.practicum.main.service.request.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
public class PrivateController {

    public final EventService eventService;
    private final RequestService requestService;

    @GetMapping(value = "/events")
    public Collection<EventShortDto> findAllEvensCreatedByUser(@PathVariable @Positive long userId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.findAllCreatedByUser(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping(value = "/events")
    public EventFullDto updateEventByUser(@PathVariable @Positive long userId,
                                          @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateByUser(userId, updateEventRequest);
    }

    @PostMapping(value = "/events")
    public EventFullDto saveEvent(@PathVariable @Positive long userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.save(userId, newEventDto);
    }

    @GetMapping(value = "/events/{eventId}")
    public EventFullDto findByInitiatorIdAndEventId(@PathVariable @Positive long userId,
                                                    @PathVariable @Positive long eventId) {
        return eventService.findByInitiatorIdAndEventId(userId, eventId);
    }

    @PatchMapping(value = "/events/{eventId}")
    public EventFullDto cancelEventByIdAddedByInitiator(@PathVariable @Positive long userId,
                                                        @PathVariable @Positive long eventId) {
        return eventService.cancelByInitiator(userId, eventId);
    }

    @GetMapping(value = "/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> findRequestByUserAndEventId(@PathVariable @Positive long userId,
                                                                           @PathVariable @Positive long eventId) {
        return requestService.findAllByEventId(userId, eventId);
    }

    @PatchMapping(value = "/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable @Positive long userId,
                                                  @PathVariable @Positive long eventId,
                                                  @PathVariable @Positive long reqId) {
        return requestService.confirm(userId, eventId, reqId);
    }

    @PatchMapping(value = "/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable @Positive long userId,
                                                 @PathVariable @Positive long eventId,
                                                 @PathVariable @Positive long reqId) {
        return requestService.reject(userId, eventId, reqId);
    }

    @GetMapping(value = "/requests")
    public Collection<ParticipationRequestDto> findAllRequestsByRequester(@PathVariable @Positive long userId) {
        return requestService.findAllByRequesterId(userId);
    }

    @PostMapping(value = "/requests")
    public ParticipationRequestDto saveRequest(@PathVariable @Positive long userId,
                                               @RequestParam(required = false) Long eventId) {
        return requestService.save(userId, eventId);
    }

    @PatchMapping(value = "/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnRequest(@PathVariable @Positive long userId,
                                                    @PathVariable @Positive long requestId) {
        return requestService.cancel(userId, requestId);
    }
}
