package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.request.ParticipationRequest;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Collection<ParticipationRequest> findAllByRequesterId(Long requesterId);

    int countAllByRequesterIdAndEventId(Long requesterId, Long eventId);

    Collection<ParticipationRequest> findAllByEventId(Long eventId);
}
