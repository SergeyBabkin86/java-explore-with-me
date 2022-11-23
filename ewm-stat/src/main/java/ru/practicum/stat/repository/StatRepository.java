package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    /* @Query("select e from EndpointHit e where e.created between ?1 and ?2 and e.uri in ?3")
    Collection<EndpointHit> findAllByCreatedBetweenAndUriIn(LocalDateTime start,
                                                            LocalDateTime end,
                                                            List<String> uris); */


    @Query(value = "select app, uri, count(distinct ip) hits " +
            " from endpoint_hits " +
            " where created between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri", nativeQuery = true)
    List<Object[]> getEndpointHitsUnique(LocalDateTime start, LocalDateTime end,
                                         List<String> uris);

    @Query(value = "select app, uri, count(ip) hits " +
            " from endpoint_hits " +
            " where created between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> getEndpointHitsNotUnique(LocalDateTime startFormatted, LocalDateTime endFormatted,
                                            List<String> uris);
}

