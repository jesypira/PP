package com.pira.piraproject.user;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Spring automatically injects the repository here
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        return userRepository.save(newUser);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getInfo(UserDetailsCustom userDetails) {
        return userRepository.findById(userDetails.getId()).orElse(null);
    }
}