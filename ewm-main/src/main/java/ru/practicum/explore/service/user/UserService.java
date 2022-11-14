package ru.practicum.explore.service.user;

import ru.practicum.explore.model.user.dto.NewUserRequest;
import ru.practicum.explore.model.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto save(NewUserRequest newUserRequest);

    void deleteById(Long userId);

    Collection<UserDto> findAll(Long[] ids, int from, int size);
}
