package ru.practicum.main.model.category.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class CategoryDto {

    @NotNull(message = "Category id should not be null.")
    @Min(value = 1, message = "Category id should be equal or above 1.")
    Long id;

    @NotBlank(message = "Category name shouldn't be null or blank.")
    String name;
}
