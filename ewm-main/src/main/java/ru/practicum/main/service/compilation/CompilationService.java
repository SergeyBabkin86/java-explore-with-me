package ru.practicum.main.service.compilation;

import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.compilation.dto.NewCompilationDto;

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
