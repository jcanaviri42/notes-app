package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSearchStateRepositoryTest {

    @Mock
    private UserSearchStateRepository userSearchStateRepository;

    private User user;
    private List<UserSearchState> searchStates;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "test_username", "password", null, null, null, null);
        searchStates = new ArrayList<>();
    }

    @Test
    void shouldFindAllByUser() {
        when(userSearchStateRepository.findAllByUser(user)).thenReturn(searchStates);

        List<UserSearchState> retrievedStates = userSearchStateRepository.findAllByUser(user);

        assertEquals(searchStates, retrievedStates);
    }
}