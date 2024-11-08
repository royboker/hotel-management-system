package com.hotel.management.hotel_management.entity;

import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a task within the hotel management system. This class includes details
 * such as the task title, description, type, priority, status, and the employee it is
 * assigned to. It also tracks the creation date and the last update timestamp.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(name = "task_title", nullable = false, unique = true)
    private String taskTitle;  // כותרת ייחודית לכל משימה

    @Column(name = "task_description", nullable = false)
    private String taskDescription;


    /**
    * The type of task (e.g., Cleaning, Bar).
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private Type taskType;  // סוג המשימה (למשל: ניקיון, תחזוקה וכו')


    /**
     * The status of the task (e.g., NOT_STARTED, IN_PROGRESS, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name= "task status",nullable = false)
    private TaskStatus taskStatus;


    /**
     * The priority level of the task (e.g., HIGH, MEDIUM, LOW).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority", nullable = false)
    private TaskPriority taskPriority;  // עדיפות המשימה (HIGH, MEDIUM, LOW)


    /**
     * The employee assigned to the task, if any.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id",nullable = true)
    private Employee employee;

    @CreationTimestamp
    @Column(name = "Task Date Created")
    private LocalDateTime dateCreated;

    @CreationTimestamp
    @Column(name = "Last Update")
    private LocalDateTime lastUpdate;





}
