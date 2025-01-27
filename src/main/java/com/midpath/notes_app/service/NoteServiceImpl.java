package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.NoteRepository;
import com.midpath.notes_app.repository.TagRepository;
import com.midpath.notes_app.specifications.NoteSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
        return noteRepository.findByUserAndIsArchivedFalse(user);
    }

    @Override
    public List<Note> getAllArchivedNotesByUser(User user) {
        return noteRepository.findByUserAndIsArchivedTrue(user);
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
                    if (!note.getUser().equals(user))
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update.");

                    if (updatedNote.getTitle() != null) note.setTitle(updatedNote.getTitle());
                    if (updatedNote.getContent() != null) note.setContent(updatedNote.getContent());
                    return noteRepository.save(note);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found."));
    }

    @Override
    public void deleteNote(Long id, User user) {
        noteRepository.findById(id)
                .map(note -> {
                    if (!note.getUser().equals(user)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete.");
                    }
                    noteRepository.delete(note);
                    return null;
                });
    }

    @Override
    public Note addTagsToNote(Long noteId, List<Long> tagIds, User user) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found."));

        if (!note.getUser().equals(user))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update.");

        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found."));
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

    @Override
    public Boolean archiveNote(Note note) {
        try {
            note.setArchived(true);
            this.noteRepository.save(note);

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public Boolean restoreNote(Note note) {
        try {
            note.setArchived(false);
            this.noteRepository.save(note);

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public List<Note> searchNotes(String title, String content, List<Long> tagIds, List<String> tagNames, User user) {
        Specification<Note> spec = NoteSpecifications.buildSpecification(title, content, tagIds, tagNames, user);
        if (spec != null)
            return noteRepository.findAll(spec);
        else
            return noteRepository.findByUserAndIsArchivedFalse(user);
    }
}
