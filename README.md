# Hotel Management System

This is a Spring Boot application designed to manage a hotel's staff operations,
including roles for administrators, employees, and managers. 
It includes REST APIs for various management tasks and uses a MySQL database for data persistence.


## Features
- Role-based access control with roles for Admin, Manager, and Employee.
- CRUD operations(Get,Post,Put,Delete) for managing hotel-related entities like tasks, employees, and managers.
- REST APIs documented with OpenAPI and accessible through Swagger UI.
- Initial setup for the database roles and an admin user upon application startup.

## Prerequisites
- Java 17 
- MySQL server
- Spring Boot 3
- Internet access for downloading dependencies
- Spring Security 6


## Technologies Used
- Spring Boot: Backend framework.
- Spring Data JPA: ORM for database interactions.
- MySQL: Relational database.
- Swagger: API documentation.
- BCrypt: Password encryption.
- Maven: Build and dependency management.
## Setup Instructions

## Step 1: Configure the Database
Create a MySQL database named hotel_db.
Update src/main/resources/application.properties.dev with your MySQL credentials and desired database URL:

spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db 

spring.datasource.username=YOUR_MYSQL_USERNAME

spring.datasource.password=YOUR_MYSQL_PASSWORD

## Step 2: Build and Run the Application
Go to tha main run file:src\main\java\com\hotel\management\hotel_management\HotelmanagementApplication.java
and run the file to start the application

## Step 3: Access the APIs via Swagger UI
Open your browser and go to:
http://localhost:8080/swagger-ui/index.html

This provides a user-friendly interface for exploring and testing the API endpoints.

Initial Setup for Roles and Admin User
Upon first run, the application will automatically set up the required roles
(ROLE_ADMIN, ROLE_EMPLOYEE, and ROLE_MANAGER) and an initial admin user with the following credentials:

Username: admin
Password: admin (will be hashed)
Note:Remember the password it will be hashed after the first run and you won't have access to the direct password.
The admin is the 'hotel manager' he have access to do anything!!
### `Tips for the first time`

To get started with the application and understand how it works, here are some recommended actions and tips:

1. **Login**:
    - After you insert your details to the login section you get a response from the server with a bearer token value copy this long script value to one of the locks you see and press authorize after that you successfully login to the system, and now you can try use the different endpoints

2. **Create Different Types of Managers and Employees**:
    - Use the registration feature or `create` endpoints to create a few managers of different types ( cleaning manager, bar manager).
    - Similarly, create employees of various types ( cleaning, maintenance, bar) and assign them to the appropriate managers.
    - This will help in understanding the hierarchical structure between managers and employees in the system.

3. **Create Tasks of Different Types and Priorities**:
    - Create multiple tasks with different priorities (e.g., `HIGH`, `MEDIUM`, `LOW`) and types, like cleaning, maintenance tasks.
    - Assign these tasks to different employees based on their roles, making sure each task type matches the employee's type.

4. **Assign Tasks and Check Statuses**:
    - After creating tasks, start assigning them to employees.
    - You can track the task status (`NOT_STARTED`, `IN_PROGRESS`, `COMPLETED`) and see how the application handles task assignments and status updates.

5. **Experiment with Manager and Employee Relationships**:
    - Try assigning different employees to different managers.
    - You can explore how the system validates the types and ensures that employees match the manager's type.

6. **Explore Security Features**:
    - Since the project uses Spring Security, try logging in with different roles and testing access control.
    - For example managers can only manage their employees.

By following these steps, you will get a comprehensive understanding of how to use the application and explore its main features. Each feature is built to mimic a real-world hotel management system and is designed to provide a hands-on learning experience.

## Folders Structure
src/main/java: Contains the Java source code.

### `config`
Contains configuration files, primarily related to application security
### `controller`
Defines the REST API endpoints to handle client requests:
### `entity`
Represents the core data structures that are stored in the database
### `enums`
Defines enums used within the application for specific types
### `exception`
Handles custom exceptions and error responses for better API usability
### `payload`
Contains Data Transfer Objects (DTOs) and response structures for API requests and responses
### `repository`
Interfaces that extend Spring Data JPA to interact with the database
### `security`
Defines security-related classes for authentication and authorization
### `service`
Contains the service interfaces and their implementations for business logic
#### `impl`
Implementation classes for service interfaces
### `utils`
Utility classes and constants used throughout the project
### `resources`
Contains configuration files and static resources:






