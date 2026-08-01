package com.taskmaster.repository;

import com.taskmaster.entity.Attachment;
import com.taskmaster.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByTask(Task task);

}