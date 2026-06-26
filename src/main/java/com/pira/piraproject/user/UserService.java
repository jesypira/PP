package com.pira.piraproject.user;

import com.pira.piraproject.task.TaskService;
import com.pira.piraproject.util.DATA;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TaskService taskService;
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
            User u = user.get();
            u.setNextLevel(levelService.getLNextLevel(u.getLevel().getId()));
            return recalculateStreak(u);
        }
        return null;
    }

    public User recalculateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        boolean completedSomethingToday = taskService.existsByUserIdAndDueDateAndCompletedTrue(user.getId(), today);
        boolean completedSomethingYesterday = taskService.existsByUserIdAndDueDateAndCompletedTrue(user.getId(), yesterday);

        LocalDate lastUpdate = user.getLastStreakUpdate();

        if (completedSomethingToday) {
            if (lastUpdate == null || lastUpdate.isBefore(today)) {
                if (yesterday.equals(lastUpdate)) {
                    user.setDailyStreak(user.getDailyStreak() + 1);
                } else if (!today.equals(lastUpdate)) {
                    user.setDailyStreak(1);
                }
                user.setLastStreakUpdate(today);
                userRepository.save(user);
            }
        }
        else if (completedSomethingYesterday) {
            if (lastUpdate == null || lastUpdate.isBefore(yesterday)) {
                user.setDailyStreak(user.getDailyStreak() + 1);
                user.setLastStreakUpdate(yesterday);
                userRepository.save(user);
            }
        }
        else {
            if (user.getDailyStreak() > 0) {
                user.setDailyStreak(0);
                user.setLastStreakUpdate(null);
                userRepository.save(user);
            }
        }

        return user;
    }
}