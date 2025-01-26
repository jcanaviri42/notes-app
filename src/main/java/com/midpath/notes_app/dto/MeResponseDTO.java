package com.midpath.notes_app.dto;

import java.util.List;

public record MeResponseDTO(
        Long id,
        String username,
        String roles,
        List<NoteResponseDTO> notes,
        List<NoteResponseDTO> archivedNotes,
        List<TagResponseDTO> tags
) implements ResponseDTO {
}
