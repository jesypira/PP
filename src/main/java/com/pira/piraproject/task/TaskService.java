package com.pira.piraproject.task;

import com.pira.piraproject.user.LevelService;
import com.pira.piraproject.user.User;
import com.pira.piraproject.user.UserDetails;
import com.pira.piraproject.user.UserRepository;
import com.pira.piraproject.util.DATA;
import com.pira.piraproject.util.GoldRules;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final LevelService levelService;

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Long userId, Task task) {
        task.setCompleted(false);
        task.setUser(new User(userId));
        return taskRepository.save(task);
    }

    @Transactional
    public Optional<Task> toggleTaskStatus(UserDetails userDetails, Long taskId) {
        return taskRepository.findById(taskId).map(task -> {

            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean willBeCompleted = !task.getCompleted();
            task.setCompleted(willBeCompleted);

            int amount = willBeCompleted ? GoldRules.SIMPLE_TASK : -GoldRules.SIMPLE_TASK;
            updateGoldXp(user, amount);

            levelService.checkLevel(user);

            userRepository.save(user);
            return taskRepository.save(task);
        });
    }

    private static void updateGoldXp(User user, int amount) {
        user.setGold(user.getGold() + amount);
        user.setXp(user.getXp() + amount);
        verifyZeros(user);
    }

    private static void verifyZeros(User user) {
        if (user.getGold() < 0)
            user.setGold(0);

        if (user.getXp()< 0)
            user.setXp(0);
    }

    @Transactional
    public boolean deleteTask(UserDetails userDetails, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));;

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(task.getCompleted())
            updateGoldXp(user, -GoldRules.SIMPLE_TASK);

        userRepository.save(user);

        taskRepository.deleteById(id);
        return true;
    }
}