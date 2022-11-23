package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.model.dto.EndpointHitDto;
import ru.practicum.stat.model.dto.ViewStatsDto;
import ru.practicum.stat.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatController {

    private final StatService statService;

    @PostMapping("/hit")
    public EndpointHitDto createEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        return statService.save(endpointHitDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getEndpointHits(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam LocalDateTime start,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam LocalDateTime end,
                                                    @RequestParam List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {
        System.out.println(end + " - " + start);

        return statService.getEndpointHits(start, end, uris, unique);
    }

}
