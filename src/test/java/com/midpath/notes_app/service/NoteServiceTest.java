package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    void shouldGetAllNotesByUser() {
        User user = new User();
        List<Note> notes = new ArrayList<>();
        when(noteRepository.findByUserAndIsArchivedFalse(user)).thenReturn(notes);

        List<Note> result = noteService.getAllNotesByUser(user);

        assertEquals(notes, result);
    }

    @Test
    void shouldGetNoteById() {
        Long id = 1L;
        Note note = new Note();
        when(noteRepository.findById(id)).thenReturn(Optional.of(note));

        Optional<Note> result = noteService.getNoteById(id);

        assertEquals(Optional.of(note), result);
    }

    @Test
    void shouldCreateNote() {
        User user = new User();
        Note note = new Note();
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note result = noteService.createNote(note, user);

        assertEquals(note, result);
    }

    @Test
    void shouldUpdateNote() {
        Long id = 1L;
        User user = new User();
        Note existingNote = new Note(id, "title", "content", user, false, null, null, null);
        Note updatedNote = new Note(id, "new title", "new content", user, true, null, null, null);

        when(noteRepository.findById(id)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        Note result = noteService.updateNote(id, updatedNote, user);

        assertEquals(updatedNote.getTitle(), result.getTitle());
        assertEquals(updatedNote.getContent(), result.getContent());
        assertEquals(updatedNote.isArchived(), result.isArchived());
    }

    @Test
    void shouldNotUpdateNoteIfUserIsNotTheOwner() {
        Long id = 1L;
        User user = new User(1L, "user", "pass", null, null, null, null);
        User otherUser = new User(2L, "other", "pass", null, null, null, null);
        Note existingNote = new Note(id, "title", "content", user, false, null, null, null);
        Note updatedNote = new Note(id, "new title", "new content", otherUser, true, null, null, null);

        when(noteRepository.findById(id)).thenReturn(Optional.of(existingNote));

        assertThrows(ResponseStatusException.class, () -> noteService.updateNote(id, updatedNote, user));
    }

    @Test
    void shouldDeleteNote() {
        Long id = 1L;
        User user = new User();
        Note note = new Note(id, "title", "content", user, false, null, null, null);

        when(noteRepository.findById(id)).thenReturn(Optional.of(note));
        doNothing().when(noteRepository).delete(note);

        assertDoesNotThrow(() -> noteService.deleteNote(id, user));
    }

    @Test
    void shouldNotAddTagToNoteIfNoteNotFound() {
        User user = new User(1L, "user", "pass", null, null, null, null);
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> noteService.addTagsToNote(1L, java.util.List.of(1L), user),
                "Expected addTagsToNote() to throw, but it didn't"
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    }

    @Test
    void shouldGetNotesByTagId() {
        Long tagId = 1L;
        List<Note> notes = new ArrayList<>();
        when(noteRepository.findByTagId(tagId)).thenReturn(notes);

        List<Note> result = noteService.getNotesByTagId(tagId);

        assertEquals(notes, result);
    }

    @Test
    void shouldGetNotesByTagName() {
        String tagName = "test";
        List<Note> notes = new ArrayList<>();
        when(noteRepository.findByTagName(tagName)).thenReturn(notes);

        List<Note> result = noteService.getNotesByTagName(tagName);

        assertEquals(notes, result);
    }

    @Test
    void shouldArchiveNote() {
        Note note = new Note();
        when(noteRepository.save(note)).thenReturn(note);

        Boolean result = noteService.archiveNote(note);

        assertTrue(result);
        assertTrue(note.isArchived());
    }

    @Test
    void shouldRestoreNote() {
        Note note = new Note();
        note.setArchived(true);
        when(noteRepository.save(note)).thenReturn(note);

        Boolean result = noteService.restoreNote(note);

        assertTrue(result);
        assertFalse(note.isArchived());
    }
}
