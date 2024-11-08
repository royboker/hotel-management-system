package com.hotel.management.hotel_management.controller;

import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.payload.TaskDto;
import com.hotel.management.hotel_management.payload.TaskResponse;
import com.hotel.management.hotel_management.service.TaskService;
import com.hotel.management.hotel_management.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "CRUD REST APIs for Task Resource"
)
public class TaskController
{
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Create Task REST API",
            description = "Create a specific task and adds to the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //create task rest api
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public  ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto)
    {
        return new ResponseEntity<>(taskService.createTask(taskDto),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Tasks REST API",
            description = "Get all the tasks from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 GET"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //Get all the task in the database
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping
    public TaskResponse getAllTasks(
             @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false)int pageNo,
             @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,//by amount of post
            @RequestParam(value ="sortBy",defaultValue = AppConstants.TASK_DEFAULT_SORT_BY,required = false)String sortBy,//by value field
             @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIREACTION,required = false)String sortDir//by alphabet order
    )
    {
        return taskService.getAllTasks(pageNo,pageSize,sortBy,sortDir);
    }

    @Operation(
            summary = "Get Task By Id REST API",
            description = "Get a specific task by using id field from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //find specific task by id
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findTaskById(@PathVariable(name="id") Long id)
    {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(
            summary = "Update Task By Id REST API",
            description = "Update a specific task fields by using id  from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //update the values of specific task
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@Valid@RequestBody TaskDto taskDto,@PathVariable(name="id")Long id) {
        TaskDto taskResponse = taskService.upadteTask(taskDto, id);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);

    }

    @Operation(
            summary = "Delete Task By Id REST API",
            description = "Delete a specific task by using id from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //delete a task from the database
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(name="id") Long id)
    {
       taskService.deleteTaskById(id);
       return new ResponseEntity<>("Task Entity deleted successfully.", HttpStatus.OK);
    }

//    @Operation(
//            summary = "Get Tasks By Completed Value REST API",
//            description = "Get all the tasks by the value of completed field from the data base"
//    )
//    @SecurityRequirement(
//            name = "Bear Authentication"
//    )
//    //get all the completed tasks
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
//    @GetMapping("/completed")
//    public ResponseEntity<List<TaskDto>> getCompletedTasks() {
//        List<TaskDto> completedTasks = taskService.getCompletedTasks();
//        return ResponseEntity.ok(completedTasks);
//    }

    @Operation(
            summary = "Get Tasks By Taskprioity Value REST API",
            description = "Get all the tasks by the value of TaskPrioity field from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //get all the tasks by value of taskpriority
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/byPriority/{prioity}")
    public ResponseEntity<List<TaskDto>> gettasksBybyPriority(@PathVariable(name="prioity")TaskPriority taskPriority)
    {
        List<TaskDto> tasksByPriority = taskService.getTasksByPriority(taskPriority);
        return ResponseEntity.ok(tasksByPriority);
    }

    @Operation(
            summary = "Get Tasks By Tasktype Value REST API",
            description = "Get all the tasks by the value of Tasktype field from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //Get all the tasks by value of tasktype
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/byType/{type}")
    public ResponseEntity<List<TaskDto>> getTasksByType(@PathVariable(name="type") Type type)
    {
        List<TaskDto> tasksByType = taskService.getTasksByType(type);
        return ResponseEntity.ok(tasksByType);
    }

    @Operation(
            summary = "Search Task",
            description = "Search task by enter value for task title or task description"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping("/search")
    public ResponseEntity<List<TaskDto>> searchTasks(@RequestParam(name="query") String query)
    {
        return ResponseEntity.ok(taskService.searchTasks(query));
    }





}
