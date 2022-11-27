package ru.practicum.main.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.exceptions.ConflictException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.category.dto.CategoryDto;
import ru.practicum.main.model.category.dto.NewCategoryDto;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.main.mapper.CategoryMapper.toCategory;
import static ru.practicum.main.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main.utilities.Checker.checkCategoryExists;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Category name should be unique.");
        }
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        var category = checkCategoryExists(categoryDto.getId(), categoryRepository);
        category.setName(categoryDto.getName());
        try {
            return toCategoryDto(categoryRepository.save(category));
        } catch (RuntimeException e) {
            throw new ConflictException("Category name should be unique.");
        }
    }

    @Override
    public void delete(Long categoryId) {
        var categoryCount = eventRepository.countAllByCategoryId(categoryId);
        if (categoryCount != 0) {
            throw new RuntimeException(format("Deleting the category with id=%s is prohibited (used in %s event(s)).",
                    categoryId,
                    categoryCount));
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Collection<CategoryDto> findAll(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long categoryId) {
        return toCategoryDto(checkCategoryExists(categoryId, categoryRepository));
    }
}
