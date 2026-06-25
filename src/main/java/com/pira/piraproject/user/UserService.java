package com.pira.piraproject.user;

import com.pira.piraproject.util.DATA;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelService levelService;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        newUser.setLevel(levelService.getLevel(DATA.LEVEL_1));

        return userRepository.save(newUser);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public User getInfo(UserDetails userDetails) {
        return userRepository.findById(userDetails.getId()).orElse(null);
    }
}