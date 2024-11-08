package com.hotel.management.hotel_management.service;

import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.payload.TaskDto;
import com.hotel.management.hotel_management.payload.TaskResponse;

import java.util.List;
/**
 * Interface for task-related services in the hotel management system.
 * Provides methods for creating, retrieving, updating, and deleting tasks,
 * as well as searching tasks by priority, type, and other criteria.
 */
public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    TaskResponse getAllTasks(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskDto getTaskById(Long id);

    TaskDto upadteTask(TaskDto taskDto , Long id);

    void deleteTaskById(Long id);

//    // שיטה להחזרת כל המשימות שהושלמו
//    List<TaskDto> getCompletedTasks();

    // שיטה לחיפוש משימות לפי עדיפות
    List<TaskDto> getTasksByPriority(TaskPriority priority);

    //שיטה לחיפוש לפי סוג
    List<TaskDto> getTasksByType(Type type);

    List<TaskDto> searchTasks(String query);


}
