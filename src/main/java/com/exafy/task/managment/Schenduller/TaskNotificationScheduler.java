package com.exafy.task.managment.Schenduller;

import com.exafy.task.managment.Model.Enum.TaskPriority;
import com.exafy.task.managment.Model.Enum.TaskStatus;
import com.exafy.task.managment.Model.Task;
import com.exafy.task.managment.Repository.Implementation.TaskRepository;
import com.exafy.task.managment.Service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TaskNotificationScheduler {

    private final TaskRepository taskRepository;
    private final MailService mailService;

    // Schedule the job to run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendTaskReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadlineThresholdLow = now.plusDays(2);  // 2-day deadline threshold for Low priority
        LocalDateTime deadlineThresholdMedium = now.plusDays(4);  // 4-day deadline threshold for Low priority
        LocalDateTime deadlineThresholdHigh = now.plusDays(7);  // 7-day deadline threshold for Low priority

        List<Task> tasks = new ArrayList<>();
        tasks.addAll( taskRepository.findTasksForNotification(TaskPriority.High, TaskStatus.Pending, deadlineThresholdHigh) );
        tasks.addAll( taskRepository.findTasksForNotification(TaskPriority.Medium, TaskStatus.Pending, deadlineThresholdMedium) );
        tasks.addAll( taskRepository.findTasksForNotification(TaskPriority.Low, TaskStatus.Pending, deadlineThresholdLow) );

        tasks.forEach(x ->{
            long daysLeft = ChronoUnit.DAYS.between(x.getDueDate(),LocalDateTime.now());
            mailService.SendNotificationAboutDeadline(x,(int)daysLeft);
        });
    }



}
