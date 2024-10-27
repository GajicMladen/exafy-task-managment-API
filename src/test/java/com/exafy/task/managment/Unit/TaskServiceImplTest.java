package com.exafy.task.managment.Unit;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Exception.InvalidTaskStatusException;
import com.exafy.task.managment.Exception.RepositoryCommunicationException;
import com.exafy.task.managment.Exception.TaskNotFoundException;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import com.exafy.task.managment.Repository.Implementation.TaskRepository;
import com.exafy.task.managment.Service.Implementation.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(TaskServiceImplTest.class);

        task = new Task();
        task.setId(1);
        task.setStatus(TaskStatus.Pending);
        taskDTO = new TaskDTO(task);
    }

    @Test
    void getAllTasks_WithValidStatus_ShouldReturnTasks() {
        String status = "Pending";
        String sortBy = "priority";
        String direction = "asc";
        int page = 0;
        int size = 5;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "priority"));
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findByStatus(TaskStatus.Pending, pageable)).thenReturn(taskPage);

        Page<TaskDTO> result = taskService.getAllTasks(status, sortBy, direction, page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository, times(1)).findByStatus(TaskStatus.Pending, pageable);
    }

    @Test
    void getAllTasks_WithInvalidStatus_ShouldThrowInvalidTaskStatusException() {
        String invalidStatus = "InvalidStatus";

        Exception exception = assertThrows(InvalidTaskStatusException.class, () ->
                taskService.getAllTasks(invalidStatus, "dueDate", "asc", 0, 5));

        assertEquals("Invalid task status: InvalidStatus", exception.getMessage());
    }

    @Test
    void deleteTaskById_TaskExists_ShouldDeleteTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTaskById(1);

        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTaskById_TaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () ->
                taskService.deleteTaskById(1));

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void updateTask_WithExistingTask_ShouldUpdateAndReturnTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.updateTask(1, taskDTO);

        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_TaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () ->
                taskService.updateTask(1, taskDTO));

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void addTask_ShouldSaveAndReturnNewTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.addTask(taskDTO);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsCompleted_TaskExists_ShouldUpdateStatusAndReturnTask() {
        task.setStatus(TaskStatus.InProgress);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.markTaskAsCompleted(1);

        assertNotNull(result);
        assertEquals(TaskStatus.Completed, task.getStatus());
        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void markTaskAsCompleted_TaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () ->
                taskService.markTaskAsCompleted(1));

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetAllTasksWithoutStatusFilter() {
        Page<Task> mockTasks = new PageImpl<>(Collections.singletonList(new Task()));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockTasks);

        Page<TaskDTO> result = taskService.getAllTasks(null, "dueDate", "asc", 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAllTasksWithStatusFilter() {
        Page<Task> mockTasks = new PageImpl<>(Collections.singletonList(new Task()));
        when(taskRepository.findByStatus(any(TaskStatus.class), any(Pageable.class))).thenReturn(mockTasks);

        Page<TaskDTO> result = taskService.getAllTasks("Completed", "dueDate", "asc", 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAllTasksWithInvalidStatus() {
        assertThrows(InvalidTaskStatusException.class, () -> taskService.getAllTasks("InvalidStatus", "dueDate", "asc", 0, 10));
    }

    @Test
    void testGetAllTasksWithNullSortBy() {
        Page<Task> mockTasks = new PageImpl<>(Collections.singletonList(new Task()));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockTasks);

        Page<TaskDTO> result = taskService.getAllTasks(null, null, "asc", 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAllTasksWithDescendingOrder() {
        Page<Task> mockTasks = new PageImpl<>(Collections.singletonList(new Task()));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockTasks);

        Page<TaskDTO> result = taskService.getAllTasks(null, "dueDate", "desc", 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testDeleteTaskByIdWithNonexistentId() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(1));
    }

    @Test
    void testDeleteTaskByIdSuccess() {
        Task task = new Task();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        taskService.deleteTaskById(1);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTaskByIdRepositoryException() {
        Task task = new Task();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        doThrow(new RuntimeException("Delete failed")).when(taskRepository).delete(task);

        assertThrows(RepositoryCommunicationException.class, () -> taskService.deleteTaskById(1));
    }

    @Test
    void testUpdateTaskWithNonexistentId() {
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(1, taskDTO));
    }

    @Test
    void testUpdateTaskSuccess() {
        Task existingTask = new Task();
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        taskService.updateTask(1, taskDTO);

        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskRepositoryException() {
        Task existingTask = new Task();
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        doThrow(new RuntimeException("Update failed")).when(taskRepository).save(existingTask);

        assertThrows(RepositoryCommunicationException.class, () -> taskService.updateTask(1, taskDTO));
    }

    @Test
    void testAddTaskSuccess() {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task(taskDTO);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.addTask(taskDTO);

        assertNotNull(result);
    }

    @Test
    void testAddTaskRepositoryException() {
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RepositoryCommunicationException.class, () -> taskService.addTask(taskDTO));
    }

    @Test
    void testMarkTaskAsCompletedWithNonexistentId() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.markTaskAsCompleted(1));
    }

    @Test
    void testMarkTaskAsCompletedSuccess() {
        Task task = new Task();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.markTaskAsCompleted(1);

        assertEquals(TaskStatus.Completed, result.getStatus());
    }

    @Test
    void testMarkTaskAsCompletedRepositoryException() {
        Task task = new Task();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        doThrow(new RuntimeException("Save failed")).when(taskRepository).save(task);

        assertThrows(RepositoryCommunicationException.class, () -> taskService.markTaskAsCompleted(1));
    }

    @Test
    void testAddTaskSetsTimestamps() {
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setCreatedAt(LocalDateTime.now());
            t.setUpdatedAt(LocalDateTime.now());
            return t;
        });

        TaskDTO result = taskService.addTask(taskDTO);

        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdatedDate());
    }

    @Test
    void testGetAllTasksWithPagination() {
        Page<Task> mockTasks = new PageImpl<>(Collections.singletonList(new Task()));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockTasks);

        Page<TaskDTO> result = taskService.getAllTasks(null, "dueDate", "asc", 1, 5);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testMarkTaskAsCompletedAlreadyCompleted() {
        Task task = new Task();
        task.setStatus(TaskStatus.Completed);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.markTaskAsCompleted(1);

        assertEquals(TaskStatus.Completed, result.getStatus());
    }

    @Test
    void addTask_WithNullTaskDTO_ShouldThrowIllegalArgumentException() {
        assertThrows(NullPointerException.class, () ->
                taskService.addTask(null));
    }

    @Test
    void getAllTasks_WithNegativePageNumber_ShouldReturnEmptyPage() {
        Page<Task> emptyPage = Page.empty();
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<TaskDTO> result = taskService.getAllTasks(null, "dueDate", "asc", 0, 5);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllTasks_WithNegativePageSize_ShouldReturnEmptyPage() {
        assertThrows(IllegalArgumentException.class, () -> taskService.getAllTasks(null, "dueDate", "asc", 0, -5));
    }

    @Test
    void markTaskAsCompleted_WithAlreadyCompletedTask_ShouldNotChangeStatus() {
        task.setStatus(TaskStatus.Completed);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.markTaskAsCompleted(1);

        assertEquals(TaskStatus.Completed, result.getStatus());
    }

}
