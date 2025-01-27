package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSearchStateRepository extends JpaRepository<UserSearchState, Long> {
    List<UserSearchState> findAllByUser(User user);

    void deleteAllByUser(User user); // This is correct
}
