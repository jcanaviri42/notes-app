package com.midpath.notes_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@SuppressWarnings("unused")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponseWithNotesAndTags> getCurrentUserWithNotesAndTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<NoteResponse> noteResponses = user.getNotes().stream()
                .map(note -> new NoteResponse(note.getId(), note.getTitle(), note.getContent(), note.isArchived(),
                        note.getTags().stream().map(tag -> new TagResponse(tag.getId(), tag.getName())).collect(Collectors.toSet())))
                .collect(Collectors.toList());

        UserResponseWithNotesAndTags userResponse = new UserResponseWithNotesAndTags(
                user.getId(),
                user.getUsername(),
                user.getRoles(),
                noteResponses
        );

        return ResponseEntity.ok(userResponse);
    }
}
