package com.taskmaster.repository;

import com.taskmaster.entity.Comment;
import com.taskmaster.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTask(Task task);

}