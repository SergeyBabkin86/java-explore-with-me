package ru.practicum.main.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.model.event.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    int countAllByCategoryId(Long categoryId);

    Collection<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);

    Collection<Event> findAllByIdIn(Iterable<Long> ids);
}
