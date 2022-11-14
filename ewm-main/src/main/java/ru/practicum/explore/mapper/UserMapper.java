package ru.practicum.explore.mapper;

import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.dto.NewUserRequest;
import ru.practicum.explore.model.user.dto.UserDto;

public class UserMapper {

    public static User toUser(NewUserRequest userRequest) {
        return new User(null, userRequest.getName(), userRequest.getEmail());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
