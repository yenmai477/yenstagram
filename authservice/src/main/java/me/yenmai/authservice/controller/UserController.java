package me.yenmai.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import me.yenmai.authservice.exception.BadRequestException;
import me.yenmai.authservice.exception.EmailAlreadyExistsException;
import me.yenmai.authservice.exception.UsernameAlreadyExistsException;
import me.yenmai.authservice.model.Profile;
import me.yenmai.authservice.model.User;
import me.yenmai.authservice.payload.ApiResponse;
import me.yenmai.authservice.payload.SignUpRequest;
import me.yenmai.authservice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@Slf4j
public class UserController {
    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getUsername());

        User user = User
                .builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .userProfile(Profile
                        .builder()
                        .displayName(payload.getName())
                        .build())
                .build();

        try {
            userService.registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true,"User registered successfully"));
    }

}
