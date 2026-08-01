package com.taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100,
            message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500,
            message = "Description cannot exceed 500 characters")
    private String description;

    private LocalDate dueDate;

    private Long teamId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

      public LocalDate getDueDate()
     {
    return dueDate;
    }

    public void setDueDate(LocalDate dueDate)
     {
    this.dueDate = dueDate;
     }

     public Long getTeamId() 
     {
    return teamId;
   }

public void setTeamId(Long teamId)
 {
    this.teamId = teamId;
}
}