package ru.practicum.explore.model.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NewUserRequest {

    @NotBlank(message = "User name shouldn't be null or blank.")
    String name;

    @NotBlank(message = "User email shouldn't be null or blank.")
    @Email(message = "User email is incorrect.")
    String email;
}
