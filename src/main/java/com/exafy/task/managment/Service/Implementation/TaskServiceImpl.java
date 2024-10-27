package com.exafy.task.managment.Service.Implementation;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import com.exafy.task.managment.Repository.Implementation.TaskRepository;
import com.exafy.task.managment.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<TaskDTO> getAllTasks(String status, String sortBy, String direction) {
        List<Task> tasks;
        // Set default sorting to dueDate if sortBy is null
        String sortField = (sortBy != null && sortBy.equalsIgnoreCase("priority")) ? "priority" : "dueDate";
        Sort.Direction sortDirection = (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortField);

        // Apply filter if status is provided
        if (status != null) {
            TaskStatus taskStatus;
            try {
                taskStatus = TaskStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid task status: " + status);
            }
            tasks = taskRepository.findByStatus(taskStatus, sort);
        }else{
            // Retrieve all tasks if no status filter is provided
            tasks = taskRepository.findAll(sort);
        }

        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOList.add(new TaskDTO(task));
        }
        return taskDTOList;
    }

    @Override
    public void deleteTaskById(int id) {
        Task existingTask = taskRepository.getReferenceById(id);
        taskRepository.delete(existingTask);
    }

    @Override
    public TaskDTO updateTask(int id,TaskDTO taskDTO) {
        Task existingTask = taskRepository.getReferenceById(id);
        existingTask.UpdateTask(taskDTO);
        taskRepository.save(existingTask);
        return new TaskDTO(existingTask);
    }

    @Override
    public TaskDTO addTask(TaskDTO taskDTO) {
        Task newTask = new Task(taskDTO);
        newTask.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        newTask.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        Task task = taskRepository.save(newTask);
        return new TaskDTO(task);
    }

    @Override
    public TaskDTO markTaskAsCompleted(int id) {
        Task existingTask = taskRepository.getReferenceById(id);
        existingTask.setStatus(TaskStatus.Completed);
        taskRepository.save(existingTask);
        return new TaskDTO(existingTask);
    }
}
