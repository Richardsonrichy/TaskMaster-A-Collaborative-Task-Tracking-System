package com.taskmaster.service;

import com.taskmaster.dto.TaskRequest;
import com.taskmaster.dto.TaskResponse;
import com.taskmaster.entity.Task;
import com.taskmaster.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.taskmaster.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
import com.taskmaster.repository.UserRepository;
import java.time.LocalDate;
import com.taskmaster.repository.TeamRepository;
import com.taskmaster.entity.Team;
import java.util.ArrayList;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public TaskService(TaskRepository taskRepository,
                   UserRepository userRepository,
                   TeamRepository teamRepository) {

    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.teamRepository = teamRepository;
}

    public TaskResponse createTask(TaskRequest request) 
    {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    Task task = new Task();

    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setDueDate(request.getDueDate());
    task.setCompleted(false);

    if (request.getTeamId() != null) {

    Team team = teamRepository.findById(request.getTeamId())
            .orElseThrow(() -> new RuntimeException("Team not found"));

   boolean isMember = team.getMembers().stream()
        .anyMatch(member -> member.getId().equals(user.getId()));

if (!isMember) {
    throw new RuntimeException("You are not a member of this team.");
}



    task.setTeam(team);
}

    task.setUser(user);

    Task savedTask = taskRepository.save(task);

    TaskResponse response = new TaskResponse();

    response.setId(savedTask.getId());
    response.setTitle(savedTask.getTitle());
    response.setDescription(savedTask.getDescription());
    response.setDueDate(savedTask.getDueDate());
    response.setCompleted(savedTask.getCompleted());

    if (savedTask.getTeam() != null) {

    response.setTeamId(savedTask.getTeam().getId());
    response.setTeamName(savedTask.getTeam().getName());
}

    if (savedTask.getUser() != null) {

    response.setUserId(savedTask.getUser().getId());

    response.setAssignedUserName(savedTask.getUser().getName());
}

    return response;
    }

    public List<TaskResponse> getAllTasks() {

    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

   User user = (User) authentication.getPrincipal();

   List<Task> tasks = taskRepository.findByUser(user);

    return tasks.stream().map(task -> {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setCompleted(task.getCompleted());

        if (task.getTeam() != null) 
        {
    response.setTeamId(task.getTeam().getId());
    response.setTeamName(task.getTeam().getName());
       }

        if (task.getUser() != null) {
            response.setUserId(task.getUser().getId());
            response.setAssignedUserName(task.getUser().getName());
        }

        return response;

      }).toList();
}

    public TaskResponse getTaskById(Long id) {

    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    TaskResponse response = new TaskResponse();

    response.setId(task.getId());
    response.setTitle(task.getTitle());
    response.setDescription(task.getDescription());
    response.setDueDate(task.getDueDate());
    response.setCompleted(task.getCompleted());

    if (task.getTeam() != null) {
        response.setTeamId(task.getTeam().getId());
        response.setTeamName(task.getTeam().getName());
    }

    if (task.getUser() != null) {
        response.setUserId(task.getUser().getId());
        response.setAssignedUserName(task.getUser().getName());
    }

    return response;
}

    public TaskResponse updateTask(Long id, Task updatedTask) {

    Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    existingTask.setTitle(updatedTask.getTitle());
    existingTask.setDescription(updatedTask.getDescription());
    existingTask.setDueDate(updatedTask.getDueDate());
    existingTask.setCompleted(updatedTask.getCompleted());

    Task savedTask = taskRepository.save(existingTask);

    TaskResponse response = new TaskResponse();

    response.setId(savedTask.getId());
    response.setTitle(savedTask.getTitle());
    response.setDescription(savedTask.getDescription());
    response.setDueDate(savedTask.getDueDate());
    response.setCompleted(savedTask.getCompleted());

    if (savedTask.getTeam() != null) {
        response.setTeamId(savedTask.getTeam().getId());
        response.setTeamName(savedTask.getTeam().getName());
    }

    if (savedTask.getUser() != null) {
        response.setUserId(savedTask.getUser().getId());
        response.setAssignedUserName(savedTask.getUser().getName());
    }

    return response;
}
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> getTasksByCompleted(Boolean completed) {

    return taskRepository.findByCompleted(completed)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    public List<TaskResponse> searchTasksByTitle(String title) {

    List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);

    List<TaskResponse> responses = new ArrayList<>();

    for (Task task : tasks) {
        responses.add(mapToResponse(task));
    }

    return responses;
}
    public Page<TaskResponse> getTasks(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return taskRepository.findAll(pageable)
            .map(this::mapToResponse);
}

    public List<TaskResponse> getTasksSorted(String field, String direction) {

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(field).descending()
            : Sort.by(field).ascending();

    return taskRepository.findAll(sort)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

    public TaskResponse assignTask(Long taskId, Long userId) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

     if (task.getTeam() != null) {

        if (!task.getTeam().getMembers().contains(user)) {
            throw new RuntimeException("User is not a member of this team");
        }
    }

    task.setUser(user);

    Task savedTask = taskRepository.save(task);

    TaskResponse response = new TaskResponse();

    response.setId(savedTask.getId());
    response.setTitle(savedTask.getTitle());
    response.setDescription(savedTask.getDescription());
    response.setDueDate(savedTask.getDueDate());
    response.setCompleted(savedTask.getCompleted());

    response.setUserId(user.getId());
    response.setAssignedUserName(user.getName());
    return response;
   }

   public TaskResponse completeTask(Long id) {

    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    task.setCompleted(true);

    Task savedTask = taskRepository.save(task);

    TaskResponse response = new TaskResponse();

    response.setId(savedTask.getId());
    response.setTitle(savedTask.getTitle());
    response.setDescription(savedTask.getDescription());
    response.setDueDate(savedTask.getDueDate());
    response.setCompleted(savedTask.getCompleted());

    if (savedTask.getUser() != null) {
        response.setUserId(savedTask.getUser().getId());
        response.setAssignedUserName(savedTask.getUser().getName());
    }

    if (savedTask.getTeam() != null) {
        response.setTeamId(savedTask.getTeam().getId());
        response.setTeamName(savedTask.getTeam().getName());
    }

    return response;
}
  private TaskResponse mapToResponse(Task task) {

    TaskResponse response = new TaskResponse();

    response.setId(task.getId());
    response.setTitle(task.getTitle());
    response.setDescription(task.getDescription());
    response.setDueDate(task.getDueDate());
    response.setCompleted(task.getCompleted());

    if (task.getUser() != null) {
        response.setUserId(task.getUser().getId());
        response.setAssignedUserName(task.getUser().getName());
    }

    if (task.getTeam() != null) {
        response.setTeamId(task.getTeam().getId());
        response.setTeamName(task.getTeam().getName());
    }

    return response;
}
}