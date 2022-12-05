package ru.practicum.main.mapper;

import ru.practicum.main.model.location.Location;
import ru.practicum.main.model.location.dto.LocationDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .address(locationDto.getAddress())
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .rad(locationDto.getRad())
                .tags(locationDto.getTags())
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .address(location.getAddress())
                .lat(location.getLat())
                .lon(location.getLon())
                .rad(location.getRad())
                .tags(location.getTags())
                .build();
    }

    public static List<LocationDto> toLocationDtos(Collection<Location> locations) {
        return locations.stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }
}
