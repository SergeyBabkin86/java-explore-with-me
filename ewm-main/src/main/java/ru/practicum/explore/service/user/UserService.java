package ru.practicum.explore.service.user;

import ru.practicum.explore.model.user.dto.NewUserRequest;
import ru.practicum.explore.model.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto save(NewUserRequest newUserRequest);

    void deleteById(Long userId);

    Collection<UserDto> getUsersById(List<Long> ids, int from, int size);
}
