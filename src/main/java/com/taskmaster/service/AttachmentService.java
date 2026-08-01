package com.taskmaster.service;

import com.taskmaster.dto.AttachmentResponse;
import com.taskmaster.entity.Attachment;
import com.taskmaster.entity.Task;
import com.taskmaster.entity.User;
import com.taskmaster.repository.AttachmentRepository;
import com.taskmaster.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    public AttachmentService(AttachmentRepository attachmentRepository,
                             TaskRepository taskRepository) {

        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
    }

    public AttachmentResponse uploadAttachment(Long taskId,
                                               MultipartFile file) throws IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";

       File directory = new File(uploadDir);

      if (!directory.exists()) {
          directory.mkdirs();
          }

         String filePath = uploadDir + File.separator + file.getOriginalFilename();

        file.transferTo(new File(filePath));

        Attachment attachment = new Attachment();

        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath);
        attachment.setTask(task);
        attachment.setUploadedBy(user);

        Attachment savedAttachment = attachmentRepository.save(attachment);

        AttachmentResponse response = new AttachmentResponse();

        response.setId(savedAttachment.getId());
        response.setFileName(savedAttachment.getFileName());
        response.setFilePath(savedAttachment.getFilePath());
        response.setTaskId(task.getId());
        response.setUserId(user.getId());
        response.setUploadedBy(user.getName());

        return response;

   }

            public List<AttachmentResponse> getAttachments(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        List<Attachment> attachments =
                attachmentRepository.findByTask(task);

        return attachments.stream().map(attachment -> {

            AttachmentResponse response = new AttachmentResponse();

            response.setId(attachment.getId());
            response.setFileName(attachment.getFileName());
            response.setFilePath(attachment.getFilePath());
            response.setTaskId(task.getId());
            response.setUserId(attachment.getUploadedBy().getId());
            response.setUploadedBy(attachment.getUploadedBy().getName());

            return response;

        }).toList();
    }

    }