package com.midpath.notes_app.repository;

import com.midpath.notes_app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;
    private String username;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "test_username", "password", null, null, null, null);
        username = "test_username";
    }

    @Test
    void shouldFindByUsername_UserFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userRepository.findByUsername(username);

        assertEquals(Optional.of(user), retrievedUser);
    }

    @Test
    void shouldFindByUsername_UserNotFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userRepository.findByUsername(username);

        assertEquals(Optional.empty(), retrievedUser);
    }
}
