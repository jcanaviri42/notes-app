package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    List<Note> getAllNotesByUser(User user);

    Optional<Note> getNoteById(Long id);

    Note createNote(Note note, User user);

    Note updateNote(Long id, Note updatedNote, User user);

    void deleteNote(Long id, User user);

    Note addTagsToNote(Long noteId, List<Long> tagIds, User user);

    List<Note> getNotesByTagId(Long tagId);

    List<Note> getNotesByTagName(String tagName);
}
