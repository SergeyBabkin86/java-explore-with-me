package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT eh.app as app, eh.uri as uri, COUNT(DISTINCT eh.ip) AS hits FROM EndpointHit AS eh " +
            "WHERE eh.uri IN (:uris) AND eh.timestamp>:start AND eh.timestamp<:end  GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getEndpointHitsUnique(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);

    @Query(value = "SELECT eh.app as app, eh.uri as uri, COUNT(eh.ip) AS hits FROM EndpointHit AS eh" +
            " WHERE eh.uri IN (:uris) AND eh.timestamp BETWEEN :start AND :end GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getEndpointHitsNotUnique(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime endFormatted,
                                                @Param("uris") List<String> uris);

    interface ViewStatsDto {
        String getApp();

        String getUri();

        Integer getHits();
    }
}

