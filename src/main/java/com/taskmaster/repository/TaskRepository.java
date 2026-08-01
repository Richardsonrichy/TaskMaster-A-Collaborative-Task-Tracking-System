package com.taskmaster.repository;

import com.taskmaster.entity.Task;
import com.taskmaster.entity.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(Boolean completed);
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByUser(User user);

}