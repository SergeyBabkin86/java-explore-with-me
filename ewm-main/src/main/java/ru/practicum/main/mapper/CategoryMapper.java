package ru.practicum.main.mapper;

import ru.practicum.main.model.category.Category;
import ru.practicum.main.model.category.dto.CategoryDto;
import ru.practicum.main.model.category.dto.NewCategoryDto;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
