package ru.practicum.main.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.exceptions.ConflictException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.user.dto.NewUserRequest;
import ru.practicum.main.model.user.dto.UserDto;
import ru.practicum.main.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.main.mapper.UserMapper.toUser;
import static ru.practicum.main.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        try {
            return toUserDto(userRepository.save(toUser(newUserRequest)));
        } catch (RuntimeException e) {
            throw new ConflictException(format("Name='%s' already exists in data base.", newUserRequest.getName()));
        }
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getUsersByIds(List<Long> ids, PageRequest pageRequest) {
        if (ids.isEmpty()) {
            return userRepository.findAll(pageRequest).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findUsersByIdIn(ids, pageRequest).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}
