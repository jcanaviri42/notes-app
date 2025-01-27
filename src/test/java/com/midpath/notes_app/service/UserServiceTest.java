package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.NoteRepository;
import com.midpath.notes_app.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private NoteRepository noteRepository;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private UserServiceImp userService;

    @Test
    void shouldGetNotesByUser() {
        User user = new User();
        List<Note> notes = List.of(new Note());
        when(noteRepository.findByUserAndIsArchivedFalse(user)).thenReturn(notes);

        List<Note> result = userService.getNotesByUser(user);

        assertEquals(notes, result);
    }

    @Test
    void shouldGetArchivedNotesByUser() {
        User user = new User();
        List<Note> notes = List.of(new Note());
        when(noteRepository.findByUserAndIsArchivedTrue(user)).thenReturn(notes);

        List<Note> result = userService.getArchivedNotesByUser(user);

        assertEquals(notes, result);
    }

    @Test
    void shouldGetTagsByUser() {
        User user = new User();
        List<Tag> tags = List.of(new Tag());
        when(tagRepository.findByUser(user)).thenReturn(tags);

        List<Tag> result = userService.getTagsByUser(user);

        assertEquals(tags, result);
    }
}
