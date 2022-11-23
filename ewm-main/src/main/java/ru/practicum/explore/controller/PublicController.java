package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.model.compilation.dto.CompilationDto;
import ru.practicum.explore.model.event.dto.EventFullDto;
import ru.practicum.explore.model.event.dto.EventShortDto;
import ru.practicum.explore.model.event.dto.GetEventRequest;
import ru.practicum.explore.service.category.CategoryService;
import ru.practicum.explore.service.compilation.CompilationService;
import ru.practicum.explore.service.event.EventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {

    private final CategoryService categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;

    @GetMapping("/events")
    public Collection<EventShortDto> getEvents(@RequestParam String text,
                                               @RequestParam Long[] categories,
                                               @RequestParam Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {

        return eventService.findAllByFilter(GetEventRequest.of(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort), from, size);

        // FIXME: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        //  нужно сохранить в сервисе статистики
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive long eventId) {
        return eventService.findById(eventId);

        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        //       нужно сохранить в сервисе статистики
    }

    @GetMapping("/compilations")
    public Collection<CompilationDto> findAllCompilations(@RequestParam(required = false) boolean pinned,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping(value = "/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable
                                              @NotNull(message = "Category id should not be null.")
                                              @Positive(message = "Category id should be equal or above 1.") long compId) {
        return compilationService.findCompilationById(compId);
    }

    @GetMapping("/categories")
    public Collection<CategoryDto> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping(value = "/categories/{categoryId}")
    public CategoryDto findById(@PathVariable
                                @NotNull(message = "Category id should not be null.")
                                @Positive(message = "Category id should be equal or above 1.") long categoryId) {
        return categoryService.findById(categoryId);
    }

}
