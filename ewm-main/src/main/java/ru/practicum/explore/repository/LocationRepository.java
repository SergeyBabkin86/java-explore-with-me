package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.event.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
