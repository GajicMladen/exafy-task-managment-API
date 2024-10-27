package com.exafy.task.managment.Service.Implementation;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Model.Task;
import com.exafy.task.managment.Service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${task.management.email}")
    private String from;

    @Async
    @Override
    public void SendTaskCreatedMail(TaskDTO task) {
        var message = new SimpleMailMessage();

        message.setFrom(from);
        message.setSubject("Successfully created task");
        message.setText("Prepare! New Task for you is created: " + task.toString());
        message.setTo(task.getAssignedUserEmail());

        mailSender.send(message);

    }

    @Async
    @Override
    public void SendTaskUpdatedMail(TaskDTO task) {
        var message = new SimpleMailMessage();

        message.setFrom(from);
        message.setSubject("Task Update - "+task.getId().toString());
        message.setText("Update! Your Task is updated:\n"+task);
        message.setTo(task.getAssignedUserEmail());

        mailSender.send(message);

    }

    @Override
    public void SendNotificationAboutDeadline(Task task, int leftDays) {
        var message = new SimpleMailMessage();

        message.setFrom(from);
        message.setSubject(leftDays+" days more for next task: "+task.getId().toString());
        message.setText("You need to give your best! Your Task is near to deadline:\n"+task);
        message.setTo(task.getAssignedUserEmail());

        mailSender.send(message);

    }
}
