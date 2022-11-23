package ru.practicum.explore.model.compilation.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NewCompilationDto {

    @NotBlank(message = "Compilation title shouldn't be null or blank.")
    String title;

    @NotNull(message = "Compilation pinned status shouldn't be null.")
    Boolean pinned;

    @NotNull(message = "List of the events id shouldn't be null.")
    Collection<Long> events;
}
