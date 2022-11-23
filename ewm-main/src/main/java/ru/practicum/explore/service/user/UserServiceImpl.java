package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.mapper.UserMapper;
import ru.practicum.explore.model.user.dto.NewUserRequest;
import ru.practicum.explore.model.user.dto.UserDto;
import ru.practicum.explore.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.mapper.UserMapper.toUser;
import static ru.practicum.explore.mapper.UserMapper.toUserDto;
import static ru.practicum.explore.utilities.Checker.checkUserExists;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        var user = userRepository.save(toUser(newUserRequest));
        return toUserDto(user);
    }

    @Override
    public void deleteById(Long userId) {
        checkUserExists(userId, userRepository);
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getUsersById(List<Long> ids, int from, int size) {
        var pageRequest = PageRequest.of(from / size, size);

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
