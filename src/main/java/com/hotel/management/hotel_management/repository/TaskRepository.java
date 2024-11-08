package com.hotel.management.hotel_management.repository;

import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
/**
 * Repository interface for performing CRUD operations on Task entities.
 * This interface extends JpaRepository and provides methods for querying Task data.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {


    List<Task> findByTaskPriority(TaskPriority taskPriority);

    List<Task> findByTaskType(Type type);

    @Query("SELECT t FROM Task t WHERE " +
            "t.taskTitle LIKE CONCAT('%',:query,'%') " +
            "OR t.taskDescription LIKE CONCAT('%',:query,'%')")
    List<Task> searchTasks(String query);
}

