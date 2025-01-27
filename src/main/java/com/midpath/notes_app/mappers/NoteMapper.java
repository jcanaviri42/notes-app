package com.midpath.notes_app.mappers;

import com.midpath.notes_app.dto.NoteResponseDTO;
import com.midpath.notes_app.dto.TagResponseDTO;
import com.midpath.notes_app.model.Note;

import java.util.List;
import java.util.stream.Collectors;

public class NoteMapper {

    public static NoteResponseDTO mapToNoteResponseDTO(Note note) {
        if (note == null) {
            return null;
        }

        List<TagResponseDTO> tagDTOs = note.getTags()
                .stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());

        return new NoteResponseDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                tagDTOs
        );
    }
}
