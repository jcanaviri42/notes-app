package com.midpath.notes_app.dto;

public record RegisterUserDTO(
        String username,
        String password,
        String roles
) implements ResponseDTO {
}
