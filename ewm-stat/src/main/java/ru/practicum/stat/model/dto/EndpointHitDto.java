package ru.practicum.stat.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class EndpointHitDto {

    @NotBlank(message = "App shouldn't be null or blank.")
    String app;

    @NotBlank(message = "Uri shouldn't be null or blank.")
    String uri;

    @NotBlank(message = "Ip shouldn't be null or blank.")
    String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
