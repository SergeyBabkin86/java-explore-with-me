package ru.practicum.main.model.location.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {

    Long id;

    @NotBlank(message = "Name shouldn't be blank.")
    String address;

    @Positive
    @NotNull(message = "Latitude shouldn't be null.")
    Double lat;

    @Positive
    @NotNull(message = "Longitude shouldn't be null.")
    Double lon;

    @Positive
    @NotNull(message = "Radius shouldn't be null.")
    Integer rad;

    Set<String> tags = new HashSet<>();
}
