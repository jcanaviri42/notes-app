package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteRepositoryTest {

    @Mock
    private NoteRepository noteRepository;

    private User user;
    private List<Note> archivedNotes;
    private List<Note> activeNotes;
    private Tag tag;
    private List<Note> notesWithTag;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "test", "test", null, null, null, null);
        archivedNotes = new ArrayList<>();
        activeNotes = new ArrayList<>();
        tag = new Tag(1L, "work", null, null);
        notesWithTag = new ArrayList<>();
    }

    @Test
    void shouldFindByUserAndIsArchivedTrue() {
        when(noteRepository.findByUserAndIsArchivedTrue(user)).thenReturn(archivedNotes);

        List<Note> retrievedNotes = noteRepository.findByUserAndIsArchivedTrue(user);

        assertEquals(archivedNotes, retrievedNotes);
    }

    @Test
    void shouldFindByUserAndIsArchivedFalse() {
        when(noteRepository.findByUserAndIsArchivedFalse(user)).thenReturn(activeNotes);

        List<Note> retrievedNotes = noteRepository.findByUserAndIsArchivedFalse(user);

        assertEquals(activeNotes, retrievedNotes);
    }

    @Test
    void shouldFindByTagId() {
        when(noteRepository.findByTagId(tag.getId())).thenReturn(notesWithTag);

        List<Note> retrievedNotes = noteRepository.findByTagId(tag.getId());

        assertEquals(notesWithTag, retrievedNotes);
    }

    @Test
    void shouldFindByTagName() {
        String tagName = "work";
        when(noteRepository.findByTagName(tagName)).thenReturn(notesWithTag);

        List<Note> retrievedNotes = noteRepository.findByTagName(tagName);

        assertEquals(notesWithTag, retrievedNotes);
    }
}
