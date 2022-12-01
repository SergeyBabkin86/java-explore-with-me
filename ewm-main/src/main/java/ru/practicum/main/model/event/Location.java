package ru.practicum.main.model.event;

import lombok.*;

@Value
@Builder
public class Location {
    Double lat;
    Double lon;
}
