package ru.practicum.explore.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.event.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query("select count(e) from Event e where e.category.id = ?1")
    int countAllByCategory_Id(Long categoryId);

    @Query("select e from Event e where e.initiator.id = ?1")
    Collection<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    @Query("select e from Event e where e.id = ?1 and e.initiator.id = ?2")
    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Query("select e from Event e where e.id in ?1")
    Collection<Event> findAllByIdIn(Iterable<Long> ids);
}
