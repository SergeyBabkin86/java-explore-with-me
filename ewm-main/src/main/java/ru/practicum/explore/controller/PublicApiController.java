package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.service.category.CategoryService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicApiController {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping(value = "/{categoryId}")
    public CategoryDto findById(@PathVariable
                                @NotNull(message = "Category id should not be null.")
                                @Min(value = 1, message = "Category id should be equal or above 1.") long categoryId) {
        return categoryService.findById(categoryId);
    }

}
