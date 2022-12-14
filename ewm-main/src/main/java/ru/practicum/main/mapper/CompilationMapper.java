package ru.practicum.main.mapper;

import ru.practicum.main.model.compilation.Compilation;
import ru.practicum.main.model.compilation.dto.CompilationDto;
import ru.practicum.main.model.compilation.dto.NewCompilationDto;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.main.mapper.EventMapper.*;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(toEventShortDto(compilation.getEvents()))
                .build();
    }

    public static Collection<CompilationDto> compilationDtoCollection(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
