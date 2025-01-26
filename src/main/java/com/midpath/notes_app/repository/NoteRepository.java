package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}
