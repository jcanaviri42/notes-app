package com.midpath.notes_app.controller;

import com.midpath.notes_app.dto.*;
import com.midpath.notes_app.model.User;
import com.midpath.notes_app.repository.UserRepository;
import com.midpath.notes_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@SuppressWarnings("unused")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody AuthRequestDTO request) {
        try {
            if (request.username() == null || request.password() == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorDTO("Missing one of the fields."));

            String token = userService.login(request.username(), request.password());
            if (token != null)
                return ResponseEntity.ok(new AuthResponseDTO(token));

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorDTO("Could not login."));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorDTO("Username or Password Incorrect."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterUserDTO request) {
        try {
            if (request.username() == null || request.password() == null || request.roles() == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorDTO("Missing one of the fields."));

            User registered = userService.register(request.username(), request.password(), request.roles());
            if (registered != null)
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new UserResponseDTO(
                                registered.getId(),
                                registered.getUsername(),
                                registered.getRoles())
                        );
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO("Username already exists."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("Internal Server Error"));
        }
    }
}
