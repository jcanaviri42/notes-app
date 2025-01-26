package com.midpath.notes_app.service;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.NoteRepository;
import com.midpath.notes_app.repository.TagRepository;
import com.midpath.notes_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class UserServiceImp implements UserService {

    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        var userDetails = service.loadUserByUsername(username);
        if (userDetails != null)
            return jwtService.generateToken(userDetails.getUsername());

        return null;
    }

    @Override
    public User register(String username, String password, String roles) {
        if (userRepository.findByUsername(username).isPresent())
            return null;

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public List<Note> getNotesByUser(User user) {
        return this.noteRepository.findByUserAndIsArchivedFalse(user);
    }

    @Override
    public List<Note> getArchivedNotesByUser(User user) {
        return this.noteRepository.findByUserAndIsArchivedTrue(user);
    }

    @Override
    public List<Tag> getTagsByUser(User user) {
        return this.tagRepository.findByUser(user);
    }
}
