package com.midpath.notes_app.dto;

import java.util.List;
import java.util.Set;

public record MeResponseDTO(
        Long id,
        String username,
        Set<String> roles,
        List<NoteResponseDTO> notes,
        List<TagResponseDTO> tags
) {
}
