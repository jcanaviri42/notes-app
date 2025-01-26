package com.midpath.notes_app.dto;

public record ErrorDTO(
        String message
) implements ResponseDTO {
}
