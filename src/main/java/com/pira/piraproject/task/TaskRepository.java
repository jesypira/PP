package com.pira.piraproject.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.dueDate = :date " +
            "ORDER BY t.completed ASC, t.completedAt DESC, t.id DESC ")
    List<Task> findByUserIdAndDueDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    boolean existsByUserIdAndDueDateAndCompletedTrue(Long id, LocalDate dueDate);
}
