package com.exafy.task.managment.Service;

import com.exafy.task.managment.DTO.TaskDTO;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<TaskDTO> getAllTasks(String status, String sortBy, String direction, int page, int size);
    void deleteTaskById(int id);
    TaskDTO updateTask(int id,TaskDTO task);
    TaskDTO addTask(TaskDTO task);
    TaskDTO markTaskAsCompleted(int id);
}
