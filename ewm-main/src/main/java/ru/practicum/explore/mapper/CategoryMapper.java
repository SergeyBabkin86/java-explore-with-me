package ru.practicum.explore.mapper;

import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.model.category.dto.NewCategoryDto;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(null, newCategoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
