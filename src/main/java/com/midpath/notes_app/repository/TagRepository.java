package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByUser(User user);

    Optional<Tag> findByNameAndUser(String name, User user);

}
