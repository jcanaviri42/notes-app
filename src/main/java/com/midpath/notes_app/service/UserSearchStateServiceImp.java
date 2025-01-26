package com.midpath.notes_app.service;

import com.midpath.notes_app.model.User;
import com.midpath.notes_app.model.UserSearchState;
import com.midpath.notes_app.repository.UserSearchStateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class UserSearchStateServiceImp implements UserSearchStateService {

    @Autowired
    private UserSearchStateRepository userSearchStateRepository;

    @Override
    public void saveState(User user, String title, String content, List<Long> tagIds, List<String> tagNames) {
        try {
            var state = new UserSearchState();
            state.setUser(user);
            if (title != null) state.setTitleFilter(title);
            if (content != null) state.setContentFilter(content);
            if (tagIds != null) state.setTagIdsFilter(tagIds);
            if (tagNames != null) state.setTagNames(tagNames);

            userSearchStateRepository.save(state);
        } catch (Exception ignored) {
        }
    }

    @Override
    @Transactional
    public void deleteStates(User user) {
        try {
            userSearchStateRepository.deleteAllByUser(user);
        } catch (Exception ignored) {
        }
    }
}
