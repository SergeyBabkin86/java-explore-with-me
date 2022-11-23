package ru.practicum.stat.model.dto;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ViewStatsDto {

    String app;

    String uri;

    Long hits;
}
