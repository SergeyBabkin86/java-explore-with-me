package ru.practicum.main.service.compilation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.compilation.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {

    Collection<CompilationDto> findAll(Boolean pinned, PageRequest pageRequest);

    CompilationDto findCompilationById(Long compId);

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    void deleteEventFromComp(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void unpinCompilation(Long compId);

    void pinCompilation(Long compId);
}
