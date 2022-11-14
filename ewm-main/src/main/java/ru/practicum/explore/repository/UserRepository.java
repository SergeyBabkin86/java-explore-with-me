package ru.practicum.explore.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id in ?1")
    List<User> findAllByIdIn (Iterable <Long> ids, PageRequest pageRequest);

}
