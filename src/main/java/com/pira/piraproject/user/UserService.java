package com.pira.piraproject.user;

import com.pira.piraproject.util.DATA;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

        newUser.setNextLevel(levelService.getLNextLevel(DATA.LEVEL_1));

        return userRepository.save(newUser);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                user.get().setNextLevel(levelService.getLNextLevel(user.get().getLevel().getId()));
                return Optional.of(user.get());
            }
        }
        return Optional.empty();
    }

    public User getInfo(UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent()) {
            user.get().setNextLevel(levelService.getLNextLevel(user.get().getLevel().getId()));
            return user.get();
        }
        return null;
    }
}