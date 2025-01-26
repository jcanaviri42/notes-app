package com.midpath.notes_app.dto;

import java.util.List;

public record NoteResponseDTO(
        Long id,
        String title,
        String content,
        List<TagResponseDTO> tags
) implements ResponseDTO {
}
