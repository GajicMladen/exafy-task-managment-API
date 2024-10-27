package com.exafy.task.managment.Repository.Implementation;

import com.exafy.task.managment.Model.Enum.TaskPriority;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE (t.priority = :priority AND t.status = :status) OR t.dueDate <= :deadline")
    List<Task> findTasksForNotification(
            @Param("priority") TaskPriority priority,
            @Param("status") TaskStatus status,
            @Param("deadline") LocalDateTime deadline);
}
