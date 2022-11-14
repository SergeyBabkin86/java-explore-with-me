package ru.practicum.explore.model.category.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
//@NoArgsConstructor(force = true)
public class CategoryDto {

    @NotNull(message = "Category id should not be null.")
    @Min(value = 1, message = "Category id should be equal or above 1.")
    Long id;

    @NotBlank(message = "Category name shouldn't be null or blank.")
    String name;
}
