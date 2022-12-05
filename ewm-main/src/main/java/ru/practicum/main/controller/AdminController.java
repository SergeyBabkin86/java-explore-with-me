package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.category.dto.CategoryDto;
import ru.practicum.main.model.category.dto.NewCategoryDto;
import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.compilation.dto.NewCompilationDto;
import ru.practicum.main.model.event.dto.AdminUpdateEventRequest;
import ru.practicum.main.model.event.dto.EventFullDto;
import ru.practicum.main.model.location.dto.LocationDto;
import ru.practicum.main.model.user.dto.NewUserRequest;
import ru.practicum.main.model.user.dto.UserDto;
import ru.practicum.main.service.category.CategoryService;
import ru.practicum.main.service.compilation.CompilationService;
import ru.practicum.main.service.event.EventService;
import ru.practicum.main.service.location.LocationService;
import ru.practicum.main.service.user.UserService;
import ru.practicum.main.utilities.GetEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final LocationService locationService;

    @GetMapping("/events")
    public Collection<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {

        return eventService.findAllByAdmin(GetEventRequest.of(users,
                states,
                categories,
                rangeStart,
                rangeEnd), PageRequest.of(from / size, size));
    }

    @PutMapping(value = "/events/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable @Positive(message = "Event id should be >= 1.") long eventId,
                                      @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventService.updateByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping(value = "/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @Positive(message = "Event id should be >= 1.") long eventId) {
        return eventService.publish(eventId);
    }

    @PatchMapping(value = "/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable @Positive(message = "Event id should be >= 1.") long eventId) {
        return eventService.reject(eventId);
    }

    @PostMapping(value = "/categories")
    public CategoryDto saveCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.save(newCategoryDto);
    }

    @PatchMapping(value = "/categories")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping(value = "/categories/{categoryId}")
    public void deleteCategoryById(@PathVariable @Positive(message = "Category id should be >= 1.") long categoryId) {
        categoryService.delete(categoryId);
    }

    @GetMapping(value = "/users")
    public Collection<UserDto> getUsersById(@RequestParam List<Long> ids,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return userService.getUsersByIds(ids, PageRequest.of(from / size, size));
    }

    @PostMapping(value = "/users")
    public UserDto saveUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.save(newUserRequest);
    }

    @DeleteMapping(value = "/users/{userId}")
    public void deleteUserById(@PathVariable @Positive(message = "User id should be >= 1.") long userId) {
        userService.deleteById(userId);
    }

    @PostMapping(value = "/compilations")
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.save(newCompilationDto);
    }

    @DeleteMapping(value = "/compilations/{compId}")
    public void deleteCompilation(@PathVariable @Positive(message = "Compilation id should be >= 1.") long compId) {
        compilationService.deleteById(compId);
    }

    @DeleteMapping(value = "/compilations/{compId}/events/{eventId}")
    public void deleteEventFromComp(@PathVariable @Positive(message = "Compilation id should be >= 1.") long compId,
                                    @PathVariable @Positive(message = "Event id should be >= 1.") long eventId) {
        compilationService.deleteEventFromComp(compId, eventId);
    }

    @PatchMapping(value = "/compilations/{compId}/events/{eventId}")
    public void addEventToComp(@PathVariable @Positive(message = "Event id should be >= 1.") long compId,
                               @PathVariable @Positive(message = "Event id should be >= 1.") long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping(value = "/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable @Positive(message = "Compilation id should be >= 1.") long compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping(value = "/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable @Positive(message = "Compilation id should be >= 1.") long compId) {
        compilationService.pinCompilation(compId);
    }

    @PostMapping(value = "/locations")
    public LocationDto saveLocation(@Valid @RequestBody LocationDto locationDto) {
        return locationService.save(locationDto);
    }

    @PatchMapping(value = "/locations")
    public LocationDto updateLocation(@Valid @RequestBody LocationDto locationDto) {
        return locationService.update(locationDto);
    }

    @DeleteMapping("/locations/{locationId}")
    public void deleteLocation(@PathVariable @Positive(message = "Location id should be >= 1.") long locationId) {
        locationService.delete(locationId);
    }
}
