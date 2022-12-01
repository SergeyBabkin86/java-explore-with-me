package ru.practicum.stat.service;

import ru.practicum.stat.model.dto.EndpointHitDto;
import ru.practicum.stat.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    EndpointHitDto save(EndpointHitDto endpointHitDto);

    List<ViewStats> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
