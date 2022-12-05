package ru.practicum.main.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.model.location.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long>, QuerydslPredicateExecutor<Location> {

    @Query(value = "SELECT l FROM Location l WHERE distance(l.lat, l.lon, :lat, :lon) <= :radius")
    List<Location> findInArea(Double lat, Double lon, Integer radius, PageRequest pageRequest);
}


