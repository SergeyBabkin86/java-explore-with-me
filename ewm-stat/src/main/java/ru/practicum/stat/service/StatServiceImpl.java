package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.model.dto.EndpointHitDto;
import ru.practicum.stat.model.dto.ViewStats;
import ru.practicum.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.stat.mapper.StatMapper.toEndpointHit;
import static ru.practicum.stat.mapper.StatMapper.toEndpointHitDto;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        var endpointHit = statRepository.save(toEndpointHit(endpointHitDto));
        return toEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStats> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<Object[]> objects;
        if (unique) {
            objects = statRepository.getEndpointHitsUnique(start, end, uris);

        } else {
            objects = statRepository.getEndpointHitsNotUnique(start, end, uris);
        }
        List<ViewStats> viewStatDtos = new ArrayList<>();

        if (!objects.isEmpty()) {
            for (Object[] object : objects) {
                var viewStatsDto = ViewStats.builder()
                        .app(object[0].toString())
                        .uri(object[1].toString())
                        .hits(Integer.valueOf(object[2].toString()))
                        .build();
                viewStatDtos.add(viewStatsDto);
            }
        }
        return viewStatDtos;
    }
}
