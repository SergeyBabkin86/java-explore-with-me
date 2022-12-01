package ru.practicum.main.mapper;

import ru.practicum.main.model.user.User;
import ru.practicum.main.model.user.dto.NewUserRequest;
import ru.practicum.main.model.user.dto.UserDto;
import ru.practicum.main.model.user.dto.UserShortDto;

public class UserMapper {

    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
