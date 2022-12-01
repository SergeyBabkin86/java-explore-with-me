package ru.practicum.main.model.stat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ViewStatDto {
    String app;
    String uri;
    Integer hits;
}
