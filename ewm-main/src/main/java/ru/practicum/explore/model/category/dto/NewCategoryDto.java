package ru.practicum.explore.model.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NewCategoryDto {

    @NotBlank(message = "Category name shouldn't be null or blank.")
    String name;
}
