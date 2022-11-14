package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.request.ParticipationRequest;

import java.util.Collection;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("select p from ParticipationRequest p where p.requester.id = ?1")
    Collection<ParticipationRequest> findAllByRequesterId (Long requesterId);

    @Query("select count(p) from ParticipationRequest p where p.requester.id = ?1 and p.event.id = ?2")
    int countAllByRequesterIdAndEventId(Long requesterId, Long eventId);

    @Query("select p from ParticipationRequest p where p.requester.id = ?1 and p.id = ?2")
    ParticipationRequest findByRequesterIdAndId(Long requesterId, Long participationRequestId);
}
