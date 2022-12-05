package ru.practicum.main.model.location.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationShortDto {

    Double lat;

    Double lon;
}
