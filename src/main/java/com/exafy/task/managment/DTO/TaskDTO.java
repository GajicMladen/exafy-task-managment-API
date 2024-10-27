package com.exafy.task.managment.DTO;

import com.exafy.task.managment.Model.Enum.TaskCategory;
import com.exafy.task.managment.Model.Enum.TaskPriority;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {


    private Integer Id;                  // ID to uniquely identify each task
    private String Title;             // Task title
    private String Description;       // Task description
    private Date DueDate;             // Task due date
    private Date CreatedDate;             // Task due date
    private Date UpdatedDate;             // Task due date
    private TaskCategory Category;    // Task category: Work, Personal, or Others
    private TaskPriority Priority;    // Task priority: Low, Medium, or High
    private TaskStatus Status;        // Task status: Pending, InProgress, or Completed
    private String AssignedUserEmail; // Email of the user assigned to the task

     public TaskDTO(Task task) {
        setId(task.getId());
        setTitle(task.getTitle());
        setDescription(task.getDescription());
        setDueDate(task.getDueDate());
        setCategory(task.getCategory());
        setPriority(task.getPriority());
        setStatus(task.getStatus());
        setAssignedUserEmail(task.getAssignedUserEmail());
        setCreatedDate(task.getCreatedAt());
        setUpdatedDate(task.getUpdatedAt());
     }
}
