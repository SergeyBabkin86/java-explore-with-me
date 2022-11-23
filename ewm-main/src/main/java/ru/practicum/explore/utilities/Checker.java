package ru.practicum.explore.utilities;

import ru.practicum.explore.errorhandling.exceptions.EntityNotFoundException;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.repository.*;

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

    public static ParticipationRequest checkPartRequestExists(Long requestId, ParticipationRequestRepository rep) {
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
