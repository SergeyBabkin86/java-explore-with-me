package ru.practicum.main.utilities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetCoordinateParam {
    Double lat;
    Double lon;
    Integer radius;

    public static GetCoordinateParam of(Double lat, Double lon, Integer radius) {
        if (lat == null || lon == null || radius == null) {
            return null;
        }
        return GetCoordinateParam.builder()
                .lat(lat)
                .lon(lon)
                .radius(radius)
                .build();
    }
}
