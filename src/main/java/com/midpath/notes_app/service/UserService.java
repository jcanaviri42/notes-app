package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;

import java.util.List;

public interface UserService {
    String login(String username, String password);

    User register(String username, String password, String roles);

    List<Note> getNotesByUser(User user);

    List<Note> getArchivedNotesByUser(User user);

    List<Tag> getTagsByUser(User user);
}
