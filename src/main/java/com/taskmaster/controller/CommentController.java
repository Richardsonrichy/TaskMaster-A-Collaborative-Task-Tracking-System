package com.taskmaster.controller;

import com.taskmaster.dto.CommentRequest;
import com.taskmaster.dto.CommentResponse;
import com.taskmaster.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{taskId}/comments")
    public CommentResponse addComment(@PathVariable Long taskId,
                                      @Valid @RequestBody CommentRequest request) {

        return commentService.addComment(taskId, request);
    }

    @GetMapping("/{taskId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long taskId) {

        return commentService.getCommentsByTask(taskId);
    }
}