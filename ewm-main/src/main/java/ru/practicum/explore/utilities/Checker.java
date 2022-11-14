package ru.practicum.explore.utilities;

import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;

import static java.lang.String.format;

public class Checker {

    public static Category checkCategoryExists(Long categoryId, CategoryRepository categoryRepository) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(format("Category with id=%s was not found.", categoryId)));
    }

    public static User checkUserExists(Long userId, UserRepository userRepository) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id=%s was not found.", userId)));
    }

    public static Event checkEventExists(Long eventId, EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(format("Event with id=%s was not found.", eventId)));
    }


}
