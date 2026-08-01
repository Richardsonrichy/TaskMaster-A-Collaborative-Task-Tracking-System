package com.taskmaster.controller;

import com.taskmaster.dto.AttachmentResponse;
import com.taskmaster.service.AttachmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public AttachmentResponse uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) throws IOException {

        return attachmentService.uploadAttachment(taskId, file);
    }

    @GetMapping
    public List<AttachmentResponse> getAttachments(
            @PathVariable Long taskId) {

        return attachmentService.getAttachments(taskId);
    }
}