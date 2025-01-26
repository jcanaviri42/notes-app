package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.*;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.UserRepository;
import com.midpath.notes_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO> me() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated())
                return ResponseEntity.status(401).build();

            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null)
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorDTO("User not found."));

            MeResponseDTO meResponseDTO = new MeResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getRoles(),
                    this.userService.getNotesByUser(user)
                            .stream()
                            .map(note ->
                                    new NoteResponseDTO(
                                            note.getId(),
                                            note.getTitle(),
                                            note.getContent(),
                                            note.getTags().stream().map(tag -> new TagResponseDTO(tag.getId(), tag.getName())).toList()))
                            .toList(),
                    this.userService.getArchivedNotesByUser(user)
                            .stream()
                            .map(note ->
                                    new NoteResponseDTO(
                                            note.getId(),
                                            note.getTitle(),
                                            note.getContent(),
                                            note.getTags().stream().map(tag -> new TagResponseDTO(tag.getId(), tag.getName())).toList()))
                            .toList(),
                    user.getTags().stream()
                            .map(tag -> new TagResponseDTO(
                                    tag.getId(),
                                    tag.getName()))
                            .toList()
            );

            return ResponseEntity.ok(meResponseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("Internal Server Error"));
        }
    }
}
