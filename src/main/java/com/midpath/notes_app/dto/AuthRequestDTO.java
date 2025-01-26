package com.midpath.notes_app.dto;

public record AuthRequestDTO(
        String username,
        String password
) implements ResponseDTO {
}
