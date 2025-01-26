package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSearchStateRepository extends JpaRepository<UserSearchState, Long> {
    Optional<UserSearchState> findByUser(User user);

    List<UserSearchState> findAllByUser(User user);

    void deleteAllByUser(User user); // This is correct
}
