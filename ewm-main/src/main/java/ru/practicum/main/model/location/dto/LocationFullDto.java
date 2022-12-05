package ru.practicum.main.model.location.dto;

import lombok.*;

import java.util.Set;

@Value
@Builder
public class LocationFullDto {

    Long id;

    String address;

    Double lat;

    Double lon;

    Integer rad;

    Set<String> tags;
}
