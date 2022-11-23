package ru.practicum.explore.service.compilation;

import ru.practicum.explore.model.compilation.dto.CompilationDto;
import ru.practicum.explore.model.compilation.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {

    Collection<CompilationDto> findAll(Boolean pinned, int from, int size);

    //TODO: "/compilation/{compId}"
    CompilationDto findCompilationById(Long compId);

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    void deleteEventFromComp(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void unpinCompilation(Long compId);

    void pinCompilation(Long compId);
}
