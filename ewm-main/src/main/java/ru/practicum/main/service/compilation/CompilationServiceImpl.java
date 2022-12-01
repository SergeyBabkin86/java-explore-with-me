package ru.practicum.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.compilation.dto.NewCompilationDto;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.Collection;
import java.util.HashSet;

import static ru.practicum.main.mapper.CompilationMapper.*;
import static ru.practicum.main.utilities.Checker.checkCompilationExists;
import static ru.practicum.main.utilities.Checker.checkEventExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Collection<CompilationDto> findAll(Boolean pinned, PageRequest pageRequest) {
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

        compilation.setEvents(new HashSet<>(events));
        compilationRepository.save(compilation);
        return toCompilationDto(compilation);
    }

    @Override
    public void deleteById(Long compId) {
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
