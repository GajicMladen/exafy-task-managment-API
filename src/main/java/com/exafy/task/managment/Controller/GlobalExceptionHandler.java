package com.exafy.task.managment.Controller;

import com.exafy.task.managment.Exception.InvalidTaskStatusException;
import com.exafy.task.managment.Exception.RepositoryCommunicationException;
import com.exafy.task.managment.Exception.TaskNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskStatusException.class)
    public ResponseEntity<String> handleInvalidTaskStatus(InvalidTaskStatusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepositoryCommunicationException.class)
    public ResponseEntity<String> handleRepositoryCommunicationException(RepositoryCommunicationException ex) {
        return new ResponseEntity<>("Error occurred wile communicating with DB:\n" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParse(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Error with provided data: "+ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred on server side: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
