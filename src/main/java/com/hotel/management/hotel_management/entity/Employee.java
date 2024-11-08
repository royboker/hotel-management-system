package com.hotel.management.hotel_management.entity;

import com.hotel.management.hotel_management.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents an employee in the hotel management system. This class extends the User class
 * and includes specific attributes and relationships pertinent to employees.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User {

    @Column(name = "employee_bonus", nullable = false)
    private BigDecimal employeeBonus = BigDecimal.ZERO;

    /**
     * The task currently assigned to the employee. This relationship is bidirectional
     * with the Task entity.
     */
    @OneToOne(mappedBy = "employee",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Task employeeTask;


    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type", nullable = false)
    private Type employeeType; // לדוגמה: ניקיון, קבלה, תחזוקה וכו'

    @Column(name = "is_employee_active", nullable = false)
    private boolean isActive;


    /**
     * The manager responsible for overseeing this employee. This is a many-to-one
     * relationship, allowing multiple employees to be managed by one manager.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    private Manager manager; // מנהל שמנהל את העובד

}
