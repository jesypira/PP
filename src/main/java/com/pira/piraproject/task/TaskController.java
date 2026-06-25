package com.pira.piraproject.task;

import com.pira.piraproject.user.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userDetails.getId()));
    }

    @PostMapping("/new")
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Task task) {
        Long userId = userDetails.getId();
        Task savedTask = taskService.createTask(userId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return taskService.toggleTaskStatus(userDetails, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        if (taskService.deleteTask(userDetails, id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}