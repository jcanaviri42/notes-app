package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.ErrorDTO;
import com.midpath.notes_app.dto.TagResponseDTO;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.UserRepository;
import com.midpath.notes_app.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tags")
@SuppressWarnings("unused")
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all the tags.
     * @return Return all the user tags.
     */
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return ResponseEntity.status(401).build();

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found."));

        return ResponseEntity.ok(tagService.getAllTags(user)
                .stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                .toList()
        );
    }

    /**
     * Returns a tag by its id.
     * @param id the tag of a single id.
     * @return returns a single tag by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return tagService.getTagById(id)
                .map(tag -> ResponseEntity
                        .ok(new TagResponseDTO(tag.getId(), tag.getName())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found."));
    }

    /**
     * Creates a new tag for a user.
     * @param tag the new tag to save.
     * @return Response entity of created or an error if not.
     */
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return ResponseEntity.status(401).build();

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            Tag newTag = tagService.createTag(tag, user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new TagResponseDTO(newTag.getId(), newTag.getName()));
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode().value())
                    .body(new ErrorDTO(ex.getReason()));
        }
    }

    /**
     * Updates a tag by its id.
     * @param id an tag id.
     * @param updatedTag an object with the fields that the user wants to change.
     * @return The updated tag.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @Valid @RequestBody Tag updatedTag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            Tag updated = tagService.updateTag(id, updatedTag, user);
            return ResponseEntity.ok(new TagResponseDTO(updated.getId(), updated.getName()));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getReason());
        }
    }

    /**
     * Deletes a user tag by its id.
     * @param id an id of a tag.
     * @return Response entity with no content or an error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        try {
            tagService.deleteTag(id, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getReason());
        }
    }
}
