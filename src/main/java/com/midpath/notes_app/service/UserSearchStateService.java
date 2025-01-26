package com.midpath.notes_app.service;

import com.midpath.notes_app.model.User;

import java.util.List;

public interface UserSearchStateService {
    void saveState(User user, String title, String content, List<Long> tagIds, List<String> tagNames);

    void deleteStates(User user);
}
