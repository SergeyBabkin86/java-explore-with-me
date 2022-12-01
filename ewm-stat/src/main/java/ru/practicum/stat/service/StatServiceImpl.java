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
        List<StatRepository.ViewStatsDto> viewStatsDtos;

        if (unique) {
            viewStatsDtos = statRepository.getEndpointHitsUnique(start, end, uris);
        } else {
            viewStatsDtos = statRepository.getEndpointHitsNotUnique(start, end, uris);
        }

        List<ViewStats> viewStats = new ArrayList<>();

        if (!viewStatsDtos.isEmpty()) {
            for (StatRepository.ViewStatsDto viewStatsDto : viewStatsDtos) {
                var viewStat = ViewStats.builder()
                        .app(viewStatsDto.getApp())
                        .uri(viewStatsDto.getUri())
                        .hits(viewStatsDto.getHits())
                        .build();
                viewStats.add(viewStat);
            }
        }
        return viewStats;
    }
}
