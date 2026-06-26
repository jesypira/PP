package com.pira.piraproject.task;

import com.pira.piraproject.enums.TaskDifficulty;
import com.pira.piraproject.user.LevelService;
import com.pira.piraproject.user.User;
import com.pira.piraproject.user.UserDetails;
import com.pira.piraproject.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final LevelService levelService;

    public List<Task> getTasksByUserId(Long userId, LocalDate dueDate) {
        return taskRepository.findByUserIdAndDueDate(userId, dueDate);
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

            if(willBeCompleted)
                task.setCompletedAt(LocalDateTime.now());
            else task.setCompletedAt(null);

            updateGoldXp(user, task);

            levelService.checkLevel(user);

            userRepository.save(user);
            return taskRepository.save(task);
        });
    }

    private static void updateGoldXp(User user, Task task) {
        TaskDifficulty diff = task.getDifficulty();
        int multiplier = task.getCompleted()?1:-1;

        int xpMod = multiplier*diff.getXpReward();
        int goldMod = multiplier*diff.getGoldReward();

        user.setGold(user.getGold() + goldMod);
        user.setXp(user.getXp() + xpMod);

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

        if(task.getCompleted()){
            task.setCompleted(false);
            updateGoldXp(user, task);
        }

        userRepository.save(user);

        taskRepository.deleteById(id);
        return true;
    }
}