package ru.practicum.main.service.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main.model.user.dto.NewUserRequest;
import ru.practicum.main.model.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto save(NewUserRequest newUserRequest);

    void deleteById(Long userId);

    Collection<UserDto> getUsersByIds(List<Long> ids, PageRequest pageRequest);
}
