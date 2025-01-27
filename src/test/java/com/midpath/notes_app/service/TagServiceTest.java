package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private User user;
    private Tag tag;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");

        tag = new Tag();
        tag.setId(1L);
        tag.setName("testTag");
        tag.setUser(user);
    }

    @Test
    void getAllTagsByUser() {
        when(tagRepository.findByUser(user)).thenReturn(List.of(tag));

        List<Tag> tags = tagService.getAllTags(user);

        assertEquals(1, tags.size());
        assertEquals("testTag", tags.getFirst().getName());
    }

    @Test
    void getTagById_ExistingTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Optional<Tag> foundTag = tagService.getTagById(1L);

        assertTrue(foundTag.isPresent());
        assertEquals("testTag", foundTag.get().getName());
    }

    @Test
    void getTagById_NonExistingTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Tag> foundTag = tagService.getTagById(1L);

        assertTrue(foundTag.isEmpty());
    }

    @Test
    void createTag_Successful() {
        when(tagRepository.findByNameAndUser(tag.getName(), user)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag(tag, user);

        assertEquals("testTag", createdTag.getName());
        assertEquals(user, createdTag.getUser());
    }

    @Test
    void createTag_DuplicateName() {
        when(tagRepository.findByNameAndUser(tag.getName(), user)).thenReturn(Optional.of(tag));

        assertThrows(ResponseStatusException.class, () -> tagService.createTag(tag, user));
    }

    @Test
    void updateTag_Successful() {
        Tag updatedTag = new Tag();
        updatedTag.setName("updatedTag");
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.findByNameAndUser(updatedTag.getName(), user)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag result = tagService.updateTag(1L, updatedTag, user);

        assertEquals("updatedTag", result.getName());
    }

    @Test
    void updateTag_NotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tagService.updateTag(1L, new Tag(), user));
    }

    @Test
    void updateTag_DifferentUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        assertThrows(ResponseStatusException.class, () -> tagService.updateTag(1L, new Tag(), otherUser));
    }

    @Test
    void deleteTag_DifferentUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        assertThrows(ResponseStatusException.class, () -> tagService.deleteTag(1L, otherUser));
    }
}