package com.midpath.notes_app.dto;

import java.util.List;

public record SearchStatesResponseDTO(
        String title,
        String content,
        List<Long> tagIds,
        List<String> tagNames
) implements ResponseDTO {
}
