package com.taskmaster.controller;

import com.taskmaster.dto.TaskRequest;
import com.taskmaster.dto.TaskResponse;
import com.taskmaster.entity.Task;
import com.taskmaster.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
   public TaskResponse getTaskById(@PathVariable Long id) {
    return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
   public TaskResponse updateTask(@PathVariable Long id,
                               @Valid @RequestBody Task task) {

    return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/status/{completed}")
    public List<TaskResponse> getTasksByCompleted(@PathVariable Boolean completed) {
    return taskService.getTasksByCompleted(completed);
    }

    @GetMapping("/search")
     public List<TaskResponse> searchTasksByTitle(
        @RequestParam String title) {

      return taskService.searchTasksByTitle(title);
     }

    

    @GetMapping("/page")
   public Page<TaskResponse> getTasks(
        @RequestParam int page,
        @RequestParam int size) {

    return taskService.getTasks(page, size);
    }

    @GetMapping("/sort")
    public List<TaskResponse> getTasksSorted(
        @RequestParam String field,
        @RequestParam(defaultValue = "asc") String direction) {

    return taskService.getTasksSorted(field, direction);
}

    @PutMapping("/{taskId}/assign/{userId}")
    public TaskResponse assignTask(@PathVariable Long taskId,
                               @PathVariable Long userId) {

                                 
    return taskService.assignTask(taskId, userId);
}

@PutMapping("/{id}/complete")
public TaskResponse completeTask(@PathVariable Long id) {

    return taskService.completeTask(id);
} 

}