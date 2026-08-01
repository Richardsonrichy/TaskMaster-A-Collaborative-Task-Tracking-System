package com.taskmaster.dto;
import java.time.LocalDate;

public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private Boolean completed;
    private Long userId;
    private String assignedUserName;
    private LocalDate dueDate;
    private Long teamId;
    private String teamName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getUserId()
     {
    return userId;

    }

public void setUserId(Long userId)

 {
    this.userId = userId;
}

public String getAssignedUserName() 

{
    return assignedUserName;
}

public void setAssignedUserName(String assignedUserName) 

{
    this.assignedUserName = assignedUserName;
} 

  public LocalDate getDueDate()
     {
    return dueDate;
    }

public void setDueDate(LocalDate dueDate)
 {
    this.dueDate = dueDate;
}

public Long getTeamId() {
    return teamId;
}

public void setTeamId(Long teamId) {
    this.teamId = teamId;
}

public String getTeamName()
 {
    return teamName;
}

public void setTeamName(String teamName)
 {
    this.teamName = teamName;
}

}