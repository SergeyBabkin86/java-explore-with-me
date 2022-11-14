package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.model.category.dto.NewCategoryDto;
import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.user.dto.NewUserRequest;
import ru.practicum.explore.model.user.dto.UserDto;
import ru.practicum.explore.service.category.CategoryService;
import ru.practicum.explore.service.event.EventService;
import ru.practicum.explore.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminApiController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;

    @PostMapping(value = "/categories")
    public CategoryDto save(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.save(newCategoryDto);
    }

    @PatchMapping(value = "/categories")
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping(value = "/categories/{categoryId}")
    public void deleteCategoryById(@PathVariable
                       @NotNull(message = "Category id should not be null.")
                       @Min(value = 1, message = "Category id should be equal or above 1.") long categoryId) {
        categoryService.delete(categoryId);
    }

    @PostMapping(value = "/users")
    public UserDto save(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.save(newUserRequest);
    }

    @GetMapping(value = "/users")
    public Collection<UserDto> findAll(@RequestParam(defaultValue = "", required = false) Long[] ids,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                       @RequestParam(defaultValue = "10") @Positive int size) {
        return userService.findAll(ids, from, size);
    }

    @DeleteMapping(value = "/users/{userId}")
    public void deleteUserById(@PathVariable
                       @NotNull(message = "User id should not be null.")
                       @Min(value = 1, message = "User id should be equal or above 1.") long userId) {
        userService.deleteById(userId);
    }

    // EVENTS

    @PatchMapping(value = "/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable
                                         @NotNull(message = "Event id should not be null.")
                                         @Min(value = 1, message = "Event id should be equal or above 1.") long eventId) {
        return eventService.publish(eventId);
    }
}
