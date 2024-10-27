package com.exafy.task.managment.Model;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Model.Enum.TaskCategory;
import com.exafy.task.managment.Model.Enum.TaskPriority;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity{

    private String title;
    private String description;
    private Date dueDate;
    private TaskCategory category;
    private TaskPriority priority;
    private TaskStatus status;
    private String assignedUserEmail;

    public Task(TaskDTO taskDTO){
        setId(taskDTO.getId());
        setTitle(taskDTO.getTitle());
        setDescription(taskDTO.getDescription());
        setDueDate(taskDTO.getDueDate());
        setCategory(taskDTO.getCategory());
        setPriority(taskDTO.getPriority());
        setStatus(taskDTO.getStatus());
        setAssignedUserEmail(taskDTO.getAssignedUserEmail());
        setCreatedAt(taskDTO.getCreatedDate());
        setUpdatedAt(taskDTO.getUpdatedDate());
    }

    public void UpdateTask(TaskDTO taskDTO){
        setTitle(taskDTO.getTitle());
        setDescription(taskDTO.getDescription());
        setDueDate(taskDTO.getDueDate());
        setCategory(taskDTO.getCategory());
        setPriority(taskDTO.getPriority());
        setStatus(taskDTO.getStatus());
        setAssignedUserEmail(taskDTO.getAssignedUserEmail());
        setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }

}
