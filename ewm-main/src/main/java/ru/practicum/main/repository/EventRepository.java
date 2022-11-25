package ru.practicum.main.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.model.event.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    int countAllByCategory_Id(Long categoryId);

    Collection<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);

    Collection<Event> findAllByIdIn(Iterable<Long> ids);
}
