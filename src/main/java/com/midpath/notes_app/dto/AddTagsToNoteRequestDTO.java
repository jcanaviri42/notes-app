package com.midpath.notes_app.dto;

import java.util.List;

public record AddTagsToNoteRequestDTO(
        List<Long> tagIds
) implements ResponseDTO {
}
