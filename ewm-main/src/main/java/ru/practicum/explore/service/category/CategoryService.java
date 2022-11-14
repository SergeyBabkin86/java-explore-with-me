package ru.practicum.explore.service.category;

import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.model.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long categoryId);

    Collection<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long categoryId);
}
