package com.midpath.notes_app.service;

import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import com.midpath.notes_app.repository.UserSearchStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserSearchStateServiceTest {

    @Mock
    private UserSearchStateRepository userSearchStateRepository;

    @InjectMocks
    private UserSearchStateServiceImp userSearchStateService;

    @Test
    void shouldSaveState() {
        User user = new User();
        String title = "testTitle";
        String content = "testContent";
        List<Long> tagIds = List.of(1L);
        List<String> tagNames = List.of("testTag");

        userSearchStateService.saveState(user, title, content, tagIds, tagNames);

        verify(userSearchStateRepository).save(any(UserSearchState.class));
    }

    @Test
    void shouldDeleteStates() {
        User user = new User();

        userSearchStateService.deleteStates(user);

        verify(userSearchStateRepository).deleteAllByUser(user);
    }
}
