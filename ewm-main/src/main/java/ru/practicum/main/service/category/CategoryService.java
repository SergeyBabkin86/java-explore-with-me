package ru.practicum.main.service.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main.model.category.dto.CategoryDto;
import ru.practicum.main.model.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long categoryId);

    Collection<CategoryDto> findAll(PageRequest pageRequest);

    CategoryDto findById(Long categoryId);
}
