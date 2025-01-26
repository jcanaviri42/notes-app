package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.NoteRepository;
import com.midpath.notes_app.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Note> getAllNotesByUser(User user) {
        return noteRepository.findByUser(user);
    }

    @Override
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    public Note createNote(Note note, User user) {
        note.setUser(user);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Long id, Note updatedNote, User user) {
        return noteRepository.findById(id)
                .map(note -> {
                    if (!note.getUser().equals(user)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar esta nota");
                    }
                    note.setTitle(updatedNote.getTitle());
                    note.setContent(updatedNote.getContent());
                    note.setArchived(updatedNote.isArchived());
                    return noteRepository.save(note);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
    }

    @Override
    public void deleteNote(Long id, User user) {
        noteRepository.findById(id)
                .map(note -> {
                    if (!note.getUser().equals(user)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar esta nota");
                    }
                    noteRepository.delete(note);
                    return null;
                });
    }

    @Override
    public Note addTagsToNote(Long noteId, List<Long> tagIds, User user) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));

        if (!note.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar esta nota");
        }

        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Etiqueta con ID " + tagId + " no encontrada"));
            note.getTags().add(tag);
        }

        return noteRepository.save(note);
    }

    @Override
    public List<Note> getNotesByTagId(Long tagId) {
        return noteRepository.findByTagId(tagId);
    }

    @Override
    public List<Note> getNotesByTagName(String tagName) {
        return noteRepository.findByTagName(tagName);
    }
}
