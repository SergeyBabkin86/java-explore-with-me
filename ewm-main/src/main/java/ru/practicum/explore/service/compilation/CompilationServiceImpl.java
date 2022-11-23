package ru.practicum.explore.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.compilation.dto.CompilationDto;
import ru.practicum.explore.model.compilation.dto.NewCompilationDto;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.Collection;

import static ru.practicum.explore.mapper.CompilationMapper.*;
import static ru.practicum.explore.utilities.Checker.checkCompilationExists;
import static ru.practicum.explore.utilities.Checker.checkEventExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Collection<CompilationDto> findAll(Boolean pinned, int from, int size) {
        var pageRequest = PageRequest.of(from / size, size);
        var compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        return compilationDtoCollection(compilations);
    }

    @Override
    public CompilationDto findCompilationById(Long compId) {
        var compilation = checkCompilationExists(compId, compilationRepository);
        return toCompilationDto(compilation);
    }

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        var compilation = compilationRepository.save(toCompilation(newCompilationDto));
        var events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());

        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return toCompilationDto(compilation);
    }

    @Override
    public void deleteById(Long compId) {
        checkCompilationExists(compId, compilationRepository);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromComp(Long compId, Long eventId) {
        var compilation = checkCompilationExists(compId, compilationRepository);
        var event = checkEventExists(eventId, eventRepository);
        var events = compilation.getEvents();

        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        var compilation = checkCompilationExists(compId, compilationRepository);
        var event = checkEventExists(eventId, eventRepository);
        var events = compilation.getEvents();

        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        var compilation = checkCompilationExists(compId, compilationRepository);

        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        var compilation = checkCompilationExists(compId, compilationRepository);

        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }


}
