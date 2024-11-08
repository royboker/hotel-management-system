package com.hotel.management.hotel_management.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;  // ודא שזה הייבוא הנכון
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Task entity.
 * This class encapsulates the task-related data and includes validation constraints
 * for creating or updating tasks.
 */
@Data
public class TaskDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long taskId;

    @NotEmpty
    @Size(min = 5,message = "Task title have must be 5 characters")
    private String taskTitle;


    @NotEmpty
    @Size(min=15,message = "Task taskDescription have must be 15 characters")
    private String taskDescription;


    @NotNull(message = "Task Type cannot be null")
    private Type taskType;


    @NotNull(message = "Task Priority cannot be null")
    private TaskPriority taskPriority;


    @NotNull(message = "Task Status cannot be null")
    private TaskStatus taskStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateCreated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdate;



}
