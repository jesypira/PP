package com.pira.piraproject.task;

import com.pira.piraproject.user.User;
import com.pira.piraproject.user.UserDetailsCustom;
import com.pira.piraproject.user.UserRepository;
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

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Long userId, Task task) {
        task.setCompleted(false);
        task.setUser(new User(userId));
        return taskRepository.save(task);
    }

    @Transactional
    public Optional<Task> toggleTaskStatus(UserDetailsCustom userDetails, Long taskId) {
        return taskRepository.findById(taskId).map(task -> {

            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean willBeCompleted = !task.getCompleted();
            task.setCompleted(willBeCompleted);

            int goldChange = willBeCompleted ? GoldRules.SIMPLE_TASK : -GoldRules.SIMPLE_TASK;
            user.setGold(user.getGold() + goldChange);

            if (user.getGold() < 0) {
                user.setGold(0);
            }

            userRepository.save(user);
            return taskRepository.save(task);
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}