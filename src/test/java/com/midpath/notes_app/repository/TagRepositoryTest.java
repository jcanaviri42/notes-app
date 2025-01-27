package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagRepositoryTest {

    @Mock
    private TagRepository tagRepository;

    private User user;
    private List<Tag> tags;
    private Tag specificTag;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "test", "test", null, null, null, null);
        tags = new ArrayList<>();
        specificTag = new Tag(1L, "work", user, null);
    }

    @Test
    void shouldFindByUser() {
        when(tagRepository.findByUser(user)).thenReturn(tags);

        List<Tag> retrievedTags = tagRepository.findByUser(user);

        assertEquals(tags, retrievedTags);
    }

    @Test
    void shouldFindByNameAndUser() {
        String tagName = "work";
        when(tagRepository.findByNameAndUser(tagName, user)).thenReturn(Optional.of(specificTag));

        Optional<Tag> retrievedTag = tagRepository.findByNameAndUser(tagName, user);

        assertEquals(Optional.of(specificTag), retrievedTag);
    }

    @Test
    void shouldNotFindByNameAndUser_TagNotFound() {
        String tagName = "work";
        when(tagRepository.findByNameAndUser(tagName, user)).thenReturn(Optional.empty());

        Optional<Tag> retrievedTag = tagRepository.findByNameAndUser(tagName, user);

        assertEquals(Optional.empty(), retrievedTag);
    }
}
