package com.midpath.notes_app.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String roles
) implements ResponseDTO {
}
