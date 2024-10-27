package com.exafy.task.managment.Service.Implementation;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Exception.InvalidTaskStatusException;
import com.exafy.task.managment.Exception.RepositoryCommunicationException;
import com.exafy.task.managment.Exception.TaskNotFoundException;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import com.exafy.task.managment.Repository.Implementation.TaskRepository;
import com.exafy.task.managment.Service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Page<TaskDTO> getAllTasks(String status, String sortBy, String direction, int page, int size) {
        // Set default sorting to dueDate if sortBy is null
        String sortField = (sortBy != null && sortBy.equalsIgnoreCase("priority")) ? "priority" : "dueDate";
        Sort.Direction sortDirection = (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        Page<Task> tasks;
        // Apply filter if status is provided
        if (status != null) {
            TaskStatus taskStatus;
            try {
                taskStatus = TaskStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new InvalidTaskStatusException("Invalid task status: " + status);
            }
            tasks = taskRepository.findByStatus(taskStatus, pageable);
        } else {
            // Retrieve all tasks if no status filter is provided
            tasks = taskRepository.findAll(pageable);
        }

        return tasks.map(TaskDTO::new);
    }

    @Override
    public void deleteTaskById(int id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        try {
            taskRepository.delete(existingTask);
        } catch (Exception e) {
            throw new RepositoryCommunicationException("Error while deleting task with id: " + id + " :\n" + e.getMessage());
        }

    }

    @Override
    public TaskDTO updateTask(int id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        existingTask.UpdateTask(taskDTO);
        try {
            taskRepository.save(existingTask);
        } catch (Exception e) {
            throw new RepositoryCommunicationException("Error while updating task " + id + ": " + e.getMessage());
        }
        return new TaskDTO(existingTask);
    }

    @Override
    public TaskDTO addTask(TaskDTO taskDTO) {
        Task newTask = new Task(taskDTO);
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setUpdatedAt(LocalDateTime.now());
        try {
            taskRepository.save(newTask);
        } catch (Exception e) {
            throw new RepositoryCommunicationException("Error while adding task: " + e.getMessage());
        }
        return new TaskDTO(newTask);
    }

    @Override
    public TaskDTO markTaskAsCompleted(int id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        existingTask.setStatus(TaskStatus.Completed);
        try {
            taskRepository.save(existingTask);
        } catch (Exception e) {
            throw new RepositoryCommunicationException("Error while marking task as completed: " + e.getMessage());
        }
        return new TaskDTO(existingTask);
    }
}
