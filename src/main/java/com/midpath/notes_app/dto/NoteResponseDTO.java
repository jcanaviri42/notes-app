package com.midpath.notes_app.dto;

public record NoteResponseDTO(
        Long id,
        String title,
        String content
) {
}
