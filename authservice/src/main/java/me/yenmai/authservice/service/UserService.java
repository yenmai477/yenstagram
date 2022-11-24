package me.yenmai.authservice.service;

import me.yenmai.authservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> findAll();
    Optional<User> findByUsername(String username);
    List<User> findByUsernameIn(List<String> usernames);
    User registerUser(User user);
    User updateProfilePicture(String uri, String id);


}
