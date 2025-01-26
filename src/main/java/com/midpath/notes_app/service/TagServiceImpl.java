package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags(User user) {
        return tagRepository.findByUser(user);
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag createTag(Tag tag, User user) {
        if (tagRepository.findByNameAndUser(tag.getName(), user).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already exists.");
        }
        tag.setUser(user);
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, Tag updatedTag, User user) {
        return tagRepository.findById(id)
                .map(tag -> {
                    if (!tag.getUser().equals(user))
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update.");

                    if (!tag.getName().equals(updatedTag.getName()) &&
                            tagRepository.findByNameAndUser(updatedTag.getName(), user).isPresent())
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Already exists.");

                    if (updatedTag.getName() != null)
                        tag.setName(updatedTag.getName());
                    return tagRepository.save(tag);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found."));
    }

    @Override
    public void deleteTag(Long id, User user) {
        tagRepository.findById(id)
                .map(tag -> {
                    if (!tag.getUser().equals(user)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete the tag.");
                    }
                    tagRepository.delete(tag);
                    return null;
                });
    }
}
