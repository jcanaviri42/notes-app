package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.MeResponseDTO;
import com.midpath.notes_app.dto.NoteResponseDTO;
import com.midpath.notes_app.dto.TagResponseDTO;
import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> getCurrentUserWithNotesAndTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            return ResponseEntity.status(401).build();

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            return ResponseEntity.notFound().build();

        List<NoteResponseDTO> noteResponses = user.getNotes().stream()
                .filter(note -> !note.isArchived())
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent()))
                .collect(Collectors.toList());

        List<NoteResponseDTO> archiveNotesResponses = user.getNotes().stream()
                .filter(Note::isArchived)
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent()))
                .toList();

        List<TagResponseDTO> tagResponses = user.getTags().stream()
                .map(tag -> new TagResponseDTO(
                        tag.getId(),
                        tag.getName()))
                .toList();

        MeResponseDTO userResponse = new MeResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles(),
                noteResponses,
                archiveNotesResponses,
                tagResponses
        );

        return ResponseEntity.ok(userResponse);
    }
}
