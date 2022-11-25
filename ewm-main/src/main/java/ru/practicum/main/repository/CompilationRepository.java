package ru.practicum.main.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.compilation.Compilation;

import java.util.Collection;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Collection<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
