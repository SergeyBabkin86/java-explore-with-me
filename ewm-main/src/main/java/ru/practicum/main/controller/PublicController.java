package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.category.dto.CategoryDto;
import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.event.dto.EventFullDto;
import ru.practicum.main.model.event.dto.EventShortDto;
import ru.practicum.main.model.location.dto.LocationDto;
import ru.practicum.main.service.category.CategoryService;
import ru.practicum.main.service.compilation.CompilationService;
import ru.practicum.main.service.event.EventService;
import ru.practicum.main.service.location.LocationService;
import ru.practicum.main.utilities.GetCoordinateParam;
import ru.practicum.main.utilities.GetEventRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final LocationService locationService;

    @GetMapping("/events")
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size,
                                               HttpServletRequest servletRequest) {

        return eventService.getEvents(GetEventRequest.of(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort), PageRequest.of(from / size, size), servletRequest);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive long eventId, HttpServletRequest request) {
        return eventService.getEventById(eventId, request);
    }

    @GetMapping("/compilations")
    public Collection<CompilationDto> getAllCompilations(@RequestParam(required = false) boolean pinned,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                         @RequestParam(defaultValue = "10") @Positive int size) {
        return compilationService.findAll(pinned, PageRequest.of(from / size, size));
    }

    @GetMapping(value = "/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable
                                             @NotNull(message = "Category id should not be null.")
                                             @Positive(message = "Category id should be equal or above 1.") long compId) {
        return compilationService.findCompilationById(compId);
    }

    @GetMapping("/categories")
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.findAll(PageRequest.of(from / size, size));
    }

    @GetMapping(value = "/categories/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable
                                       @Positive(message = "Category id should be equal or above 1.") long categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping("/locations/{locationId}")
    public LocationDto getLocationById(@PathVariable
                                       @Positive(message = "Location id should be equal or above 1.") Long locationId) {
        return locationService.findById(locationId);
    }

    @GetMapping("/locations")
    public Collection<LocationDto> findLocationByParams(@RequestParam(required = false) String address,
                                                        @RequestParam(required = false) Set<String> tags,
                                                        @RequestParam(required = false) @Positive Double lat,
                                                        @RequestParam(required = false) @Positive Double lon,
                                                        @RequestParam(required = false) @Positive Integer radius,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return locationService.findByParamsInArea(address, tags, GetCoordinateParam.of(lat, lon, radius),
                PageRequest.of(from / size, size));
    }

    @GetMapping("/events/locations")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> findEventsInLocation(@RequestParam @Positive Double lat,
                                                          @RequestParam @Positive Double lon,
                                                          @RequestParam @Positive Integer radius,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.findInArea(GetCoordinateParam.of(lat, lon, radius),
                PageRequest.of(from / size, size));
    }
}
