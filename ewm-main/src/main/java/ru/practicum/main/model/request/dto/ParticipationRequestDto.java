package ru.practicum.main.model.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main.model.request.RequestStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Value
@Builder
public class ParticipationRequestDto {

    Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Participation request date shouldn't be in the future.")
    LocalDateTime created;

    @Positive(message = "Event id should be above zero.")
    Long event;

    @Positive(message = "Event id should be above zero.")
    Long requester;

    @NotBlank(message = "Participation request status shouldn't be null or blank.")
    RequestStatus status;
}
