package ru.practicum.explore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findUsersByIdIn(List<Long> ids, Pageable pageable);
}
