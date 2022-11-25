package ru.practicum.main.utilities;

import ru.practicum.main.error.exceptions.EntityNotFoundException;
import ru.practicum.main.model.category.Category;
import ru.practicum.main.model.compilation.Compilation;
import ru.practicum.main.model.event.Event;
import ru.practicum.main.model.request.ParticipationRequest;
import ru.practicum.main.model.user.User;
import ru.practicum.main.repository.*;

import static java.lang.String.format;

public class Checker {

    public static Category checkCategoryExists(Long categoryId, CategoryRepository rep) {
        return rep.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(format("Category with id=%s was not found.", categoryId)));
    }

    public static User checkUserExists(Long userId, UserRepository rep) {
        return rep.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id=%s was not found.", userId)));
    }

    public static Event checkEventExists(Long eventId, EventRepository rep) {
        return rep.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(format("Event with id=%s was not found.", eventId)));
    }

    public static ParticipationRequest checkRequestExists(Long requestId, RequestRepository rep) {
        return rep.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(format("Participation request with id=%s was not found.",
                        requestId)));
    }

    public static Compilation checkCompilationExists(Long compId, CompilationRepository compilationRepository) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(format("Compilation with id=%s was not found.",
                        compId)));
    }
}
