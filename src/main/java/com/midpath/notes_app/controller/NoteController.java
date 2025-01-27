package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.AddTagsToNoteRequestDTO;
import com.midpath.notes_app.dto.ErrorDTO;
import com.midpath.notes_app.dto.NoteResponseDTO;
import com.midpath.notes_app.dto.TagResponseDTO;
import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.UserRepository;
import com.midpath.notes_app.service.NoteService;
import jakarta.persistence.Cacheable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
@SuppressWarnings("unused")
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint that returns all the notes by user.
     * @return A list that contains all the notes by user, could be empty.
     */
    @GetMapping
    public ResponseEntity<List<NoteResponseDTO>> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<NoteResponseDTO> noteResponses = noteService.getAllNotesByUser(user)
                .stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt(),
                        note.getUpdatedAt(),
                        note.getTags()
                                .stream()
                                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                                .toList())
                ).toList();

        return ResponseEntity.ok(noteResponses);
    }

    /**
     * Endpoint to get a single note.
     * @param id The id of the note we want to get.
     * @return Response entity, the note we want or not found message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        try {
            Optional<Note> noteOptional = noteService.getNoteById(id);
            if (noteOptional.isPresent()) {
                Note note = noteOptional.get();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated())
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

                String username = authentication.getName();
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

                if (!note.getUser().equals(user))
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body("Cannot see.");

                return ResponseEntity.ok(new NoteResponseDTO(
                                note.getId(),
                                note.getTitle(),
                                note.getContent(),
                                note.getUpdatedAt(),
                                note.getCreatedAt(),
                                note.getTags()
                                        .stream()
                                        .map(tag -> new TagResponseDTO(tag.getId(), tag.getName())).toList()
                        )
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
            }

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    /**
     * Endpoint to create a new note.
     * @param note A new note to create.
     * @return NoteResponseDTO with the new note created or an error message.
     */
    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        try {
            Note createdNote = noteService.createNote(note, user);
            return new ResponseEntity<>(new NoteResponseDTO(
                    createdNote.getId(),
                    createdNote.getTitle(),
                    createdNote.getContent(),
                    createdNote.getCreatedAt(),
                    createdNote.getUpdatedAt(),
                    createdNote.getTags()
                            .stream()
                            .map(tag -> new TagResponseDTO(tag.getId(), tag.getName())).toList()
            ), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getReason());
        }
    }

    /**
     * Updates the content of a single note.
     * @param id the id of a note.
     * @param updatedNote Note object to change the values
     * @return Response entity, updated note or an error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody Note updatedNote) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            Note updated = noteService.updateNote(id, updatedNote, user);
            return ResponseEntity.ok(new NoteResponseDTO(
                            updated.getId(),
                            updated.getTitle(),
                            updated.getContent(),
                            updated.getCreatedAt(),
                            updated.getUpdatedAt(),
                            updated.getTags()
                                    .stream()
                                    .map(tag -> new TagResponseDTO(tag.getId(), tag.getName())).toList()
                    )
            );
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getReason());
        }
    }

    /**
     * Deletes a note by its id.
     * @param id An id of a note.
     * @return Response no content if everything is OK, otherwise an error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            noteService.deleteNote(id, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Correct return for DELETE
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode().value())
                    .body(ex.getReason());
        }
    }

    /**
     * Add tags to a note by its id.
     * @param id the id of a single note.
     * @param request An instance of AddTagsToNoteRequestDTO(List of tagIds).
     * @return The updated note.
     */
    @PutMapping("/{id}/tags")
    public ResponseEntity<?> addTagsToNote(
            @PathVariable Long id,
            @Valid @RequestBody AddTagsToNoteRequestDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            Note updatedNote = noteService.addTagsToNote(id, request.tagIds(), user);
            return ResponseEntity.ok(new NoteResponseDTO(
                            updatedNote.getId(),
                            updatedNote.getTitle(),
                            updatedNote.getContent(),
                            updatedNote.getCreatedAt(),
                            updatedNote.getUpdatedAt(),
                            updatedNote.getTags()
                                    .stream()
                                    .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                                    .toList()
                    )
            );
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode().value())
                    .body(ex.getReason());
        }
    }

    /**
     * Return all the user notes by its tagId.
     * @param tagId the tagId we want to retrieve.
     * @return A list with the notes founded.
     */
    @GetMapping("/tags/{tagId}")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByTagId(
            @PathVariable Long tagId) {
        List<Note> notes = noteService.getNotesByTagId(tagId);
        List<NoteResponseDTO> noteResponses = notes.stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt(),
                        note.getUpdatedAt(),
                        note.getTags()
                                .stream()
                                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                                .toList()
                ))
                .toList();
        return ResponseEntity.ok(noteResponses);
    }

    /**
     * Return all the notes by the tagName.
     * @param tagName The name to look for.
     * @return a list with all the notes founded.
     */
    @GetMapping("/tag-name/{tagName}")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByTagName(@PathVariable String tagName) {
        List<Note> notes = noteService.getNotesByTagName(tagName);
        List<NoteResponseDTO> noteResponses = notes.stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt(),
                        note.getUpdatedAt(),
                        note.getTags()
                                .stream()
                                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                                .toList()
                ))
                .toList();
        return ResponseEntity.ok(noteResponses);
    }

    /**
     * This method archives a note.
     * @param noteId the id of a note.
     * @return Response Entity with no content.
     */
    @PatchMapping("/{noteId}/archive")
    public ResponseEntity<?> archiveNote(@PathVariable Long noteId) {
        Optional<Note> optionalNote = this.noteService.getNoteById(noteId);
        if (optionalNote.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");

        Note note = optionalNote.get();
        Boolean hasBeenArchived = this.noteService.archiveNote(note);

        if (hasBeenArchived)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ErrorDTO("Not modified."));
    }

    /**
     * Restores a note that is archived.
     * @param noteId the id of a note
     * @return Response entity with no content.
     */
    @PatchMapping("/{noteId}/restore")
    public ResponseEntity<?> restoreNote(@PathVariable Long noteId) {
        Optional<Note> optionalNote = this.noteService.getNoteById(noteId);
        if (optionalNote.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");

        Note note = optionalNote.get();
        Boolean hasBeenRestored = this.noteService.restoreNote(note);

        if (hasBeenRestored)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ErrorDTO("Not modified."));
    }
}
