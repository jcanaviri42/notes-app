package com.midpath.notes_app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record NoteResponseDTO(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TagResponseDTO> tags
) implements ResponseDTO {
}
