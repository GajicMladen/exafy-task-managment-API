# Task Management Application

## Overview
This project is a Task Management application built with Spring Boot. It allows users to create, manage, and track tasks, while also providing features for sending notifications via email.

## Features
- Create, update, delete, and complete tasks
- Filter and sort tasks based on status and priority
- Send email notifications for task creation and updates
- Scheduled jobs for reminders and notifications

## Requirements
- Java 17 SDK 
- Maven
- A working email account for sending notifications (e.g., Gmail)

## Installation Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/task-management.git
   cd task-management

2. **Set Up Environment Variables Create a .env file in the root directory and add the following:**

    ```bash
    task.management.email=your-email
    task.management.email.password=your-email-password

3. **Build the Project Use Maven to build the project:**

    ```bash
   mvn clean install
   
4. **Run the Application Start the application with:**
    ```bash
    mvn spring-boot:run

5. **Access the Application The application will be accessible at http://localhost:8080/tasks , and you can see endpoints at http://localhost:8080/swagger-ui.html**


## Design Decisions and Trade-offs

### Framework Choice
Spring Boot: Selected for its simplicity and ease of integration with various components like databases, mail services, and scheduling. Spring Boot’s convention-over-configuration principle helps to quickly set up the application.
### Database Management
JPA & Hibernate: Chosen for ORM capabilities, enabling easier data management. Trade-off: This might add some overhead, but the benefits in maintainability and productivity are significant.
### Email Notifications
SMTP Protocol: Gmail's SMTP server was used for sending emails. This choice simplifies implementation but requires proper handling of email credentials and may necessitate setting up app passwords for increased security.
### Task Scheduling
Spring’s Scheduling: Utilized Spring’s built-in scheduling for periodic tasks. This decision allows for simple implementation of recurring notifications without needing external libraries.
### Error Handling
Implemented custom exceptions and a global exception handler to provide meaningful error messages and better control over error responses. This approach improves API usability but requires additional code for handling various scenarios.
### Asynchronous Processing
Future Tasks: Considered making email sending asynchronous to improve response time for API calls. This may introduce complexity in error handling and require monitoring for failures in background tasks.

## Testing
Unit Tests: All functionalities are tested. You can run the tests using:
   ```bash
   mvn test