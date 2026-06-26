package com.pira.piraproject.task;

import com.pira.piraproject.enums.Category;
import com.pira.piraproject.enums.TaskDifficulty;
import com.pira.piraproject.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    private Boolean completed = false;

    @Column(nullable = false)
    private LocalDate dueDate = LocalDate.now();

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskDifficulty difficulty = TaskDifficulty.EASY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category = Category.PERSONAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}