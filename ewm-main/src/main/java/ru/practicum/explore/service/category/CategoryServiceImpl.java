package ru.practicum.explore.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.dto.CategoryDto;
import ru.practicum.explore.model.category.dto.NewCategoryDto;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explore.mapper.CategoryMapper.toCategory;
import static ru.practicum.explore.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.explore.utilities.Checker.checkCategoryExists;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        var category = categoryRepository.save(toCategory(newCategoryDto));
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        var category = checkCategoryExists(categoryDto.getId(), categoryRepository);
        category.setName(categoryDto.getName());
        return toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long categoryId) {
        checkCategoryExists(categoryId, categoryRepository);
        var categoryCount = eventRepository.countAllByCategory_Id(categoryId);
        if (categoryCount != 0) {
            throw new RuntimeException(format("Deleting the category with id=%s is prohibited (used in %s event(s)).",
                    categoryId,
                    categoryCount));
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Collection<CategoryDto> findAll(int from, int size) {
        var page = from / size;
        var pageRequest = PageRequest.of(page, size);

        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long categoryId) {
        return toCategoryDto(checkCategoryExists(categoryId, categoryRepository));
    }
}
