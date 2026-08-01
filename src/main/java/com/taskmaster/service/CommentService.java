package com.taskmaster.service;

import org.springframework.stereotype.Service;
import com.taskmaster.dto.CommentRequest;
import com.taskmaster.dto.CommentResponse;
import com.taskmaster.entity.Comment;
import com.taskmaster.entity.Task;
import com.taskmaster.entity.User;
import com.taskmaster.repository.CommentRepository;
import com.taskmaster.repository.TaskRepository;
import com.taskmaster.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Service
public class CommentService {

private final CommentRepository commentRepository;
private final TaskRepository taskRepository;
private final UserRepository userRepository;

public CommentService(CommentRepository commentRepository,
                      TaskRepository taskRepository,
                      UserRepository userRepository) {

    this.commentRepository = commentRepository;
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
}
 
 public CommentResponse addComment(Long taskId, CommentRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    Comment comment = new Comment();

    comment.setText(request.getText());
    comment.setCreatedAt(LocalDateTime.now());

    comment.setTask(task);
    comment.setUser(user);

    Comment savedComment = commentRepository.save(comment);

    CommentResponse response = new CommentResponse();

    response.setId(savedComment.getId());
    response.setText(savedComment.getText());
    response.setCreatedAt(savedComment.getCreatedAt());

    response.setUserId(user.getId());
    response.setUserName(user.getName());

    return response;
}

public List<CommentResponse> getCommentsByTask(Long taskId) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    List<Comment> comments = commentRepository.findByTask(task);

    return comments.stream().map(comment -> {

        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setCreatedAt(comment.getCreatedAt());

        response.setUserId(comment.getUser().getId());
        response.setUserName(comment.getUser().getName());

        return response;

    }).toList();
}

}