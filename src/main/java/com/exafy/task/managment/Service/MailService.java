package com.exafy.task.managment.Service;

import com.exafy.task.managment.DTO.TaskDTO;
import com.exafy.task.managment.Model.Task;

public interface MailService {

    void SendTaskCreatedMail(TaskDTO task);
    void SendTaskUpdatedMail(TaskDTO task);
}
