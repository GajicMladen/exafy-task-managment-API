package com.exafy.task.managment.Service;

import com.exafy.task.managment.DTO.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks(String status, String sortBy, String direction);
    void deleteTaskById(int id);
    TaskDTO updateTask(int id,TaskDTO task);
    TaskDTO addTask(TaskDTO task);
    TaskDTO markTaskAsCompleted(int id);
}
