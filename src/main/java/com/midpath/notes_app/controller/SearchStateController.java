package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.NoteResponseDTO;
import com.midpath.notes_app.dto.SearchStatesResponseDTO;
import com.midpath.notes_app.dto.TagResponseDTO;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import com.midpath.notes_app.repository.UserRepository;
import com.midpath.notes_app.repository.UserSearchStateRepository;
import com.midpath.notes_app.service.NoteService;
import com.midpath.notes_app.service.UserSearchStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping
@SuppressWarnings("unused")
public class SearchStateController {

    @Autowired
    private UserSearchStateRepository userSearchStateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserSearchStateService userSearchStateService;

    @GetMapping("/user/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
            @RequestParam(value = "tagNames", required = false) List<String> tagNames
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<NoteResponseDTO> noteResponses = noteService
                .searchNotes(title, content, tagIds, tagNames, user)
                .stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getTags()
                                .stream()
                                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                                .toList()))
                .toList();

        userSearchStateService.saveState(user, title, content, tagIds, tagNames);

        return ResponseEntity.ok(noteResponses);
    }

    @GetMapping("/user/search-state")
    public ResponseEntity<?> getSearchState() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<UserSearchState> searchState = userSearchStateRepository.findAllByUser(user);

        List<SearchStatesResponseDTO> list = searchState.stream().map(state -> new SearchStatesResponseDTO(
                state.getTitleFilter(),
                state.getContentFilter(),
                state.getTagIdsFilter(),
                state.getTagNames()
        )).toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/user/search-state")
    public ResponseEntity<?> deleteSearchState() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        userSearchStateService.deleteStates(user);
        return ResponseEntity.noContent().build();
    }
}
