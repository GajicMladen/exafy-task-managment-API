package com.exafy.task.managment.Repository.Implementation;

import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
}
