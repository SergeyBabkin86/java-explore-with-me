package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stat.model.dto.EndpointHitDto;
import ru.practicum.stat.model.dto.ViewStatsDto;
import ru.practicum.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.stat.mapper.StatMapper.toEndpointHit;
import static ru.practicum.stat.mapper.StatMapper.toEndpointHitDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        var endpointHit = statRepository.save(toEndpointHit(endpointHitDto));
        return toEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<Object[]> endpointHits;
        if (unique.equals(true)) {
            endpointHits = statRepository.getEndpointHitsUnique(start, end, uris);

        } else {
            endpointHits = statRepository.getEndpointHitsNotUnique(start, end, uris);
        }
        List<ViewStatsDto> viewStatDtos = new ArrayList<>();

        if (!endpointHits.isEmpty()) {
            for (Object[] object : endpointHits) {
                log.info("---> {}", object); ///!!!!!
                var viewStatsDto = ViewStatsDto.builder()
                        .app(object[0].toString())
                        .uri(object[1].toString())
                        .hits(Long.valueOf(object[2].toString()))
                        .build();
                viewStatDtos.add(viewStatsDto);
            }
        }
        return viewStatDtos;
    }
}
