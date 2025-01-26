package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTags(User user);

    Optional<Tag> getTagById(Long id);

    Tag createTag(Tag tag, User user);

    Tag updateTag(Long id, Tag updatedTag, User user);

    void deleteTag(Long id, User user);
}
