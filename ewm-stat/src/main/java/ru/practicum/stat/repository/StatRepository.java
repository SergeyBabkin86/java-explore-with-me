package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT eh.app, eh.uri, COUNT (DISTINCT eh.ip) AS hits FROM EndpointHit AS eh " +
            "WHERE eh.uri IN (:uris) AND eh.timestamp>:start AND eh.timestamp<:end  GROUP BY eh.app, eh.uri")
    List<Object[]> getEndpointHitsUnique(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uris") List<String> uris);

    @Query("SELECT eh.app, eh.uri, count(eh.ip) AS hits FROM EndpointHit AS eh" +
            " WHERE eh.uri IN (:uris) AND eh.timestamp BETWEEN :start AND :end GROUP BY eh.app, eh.uri")
    List<Object[]> getEndpointHitsNotUnique(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime endFormatted,
                                            @Param("uris") List<String> uris);
}

