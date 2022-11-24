package me.yenmai.authservice.service;

import lombok.extern.slf4j.Slf4j;
import me.yenmai.authservice.exception.EmailAlreadyExistsException;
import me.yenmai.authservice.exception.ResourceNotFoundException;
import me.yenmai.authservice.exception.UsernameAlreadyExistsException;
import me.yenmai.authservice.model.Role;
import me.yenmai.authservice.model.User;
import me.yenmai.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        log.info("Retrieving all users");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.info("Retrieving all users");
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByUsernameIn(List<String> usernames) {
        log.info(String.format("Retrieving all users with username: %s", usernames));
        return userRepository.findByUsernameIn(usernames);
    }

    @Override
    public User registerUser(User user) {
        log.info("registering user {}", user.getUsername());

        //TODO: Refactor to validator with decorator pattern
        if(userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(List.of(Role.USER)));

        User savedUser = userRepository.save(user);
       //TODO: Send message to user!!!

        return savedUser;
    }

    @Override
    public User updateProfilePicture(String uri, String id) {
        log.info("update profile picture {} for user {}", uri, id);

        return userRepository.findById(id)
                .map(user -> {
                    user.getUserProfile().setProfilePictureUrl(uri);
                    User savedUser = userRepository.save(user);

                    //TODO: Send message to user!!

                    return savedUser;
                })
                .orElseThrow(()->new ResourceNotFoundException(String.format("user id %s not found", id)));
    }
}
