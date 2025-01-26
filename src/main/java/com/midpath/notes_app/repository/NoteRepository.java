package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserAndIsArchivedTrue(User user);

    List<Note> findByUserAndIsArchivedFalse(User user);

    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.id = :tagId")
    List<Note> findByTagId(@Param("tagId") Long tagId);

    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.name = :tagName")
    List<Note> findByTagName(@Param("tagName") String tagName);
}
