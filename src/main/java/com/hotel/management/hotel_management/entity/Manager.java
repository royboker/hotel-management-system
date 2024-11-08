package com.hotel.management.hotel_management.entity;

import com.hotel.management.hotel_management.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a manager in the hotel management system. This class extends the User class
 * and includes specific attributes and relationships pertinent to managers.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "managers")
public class Manager extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "manager_type", nullable = false)
    private Type managerType;  // סוג המנהל (ניקיון, קבלה וכו')

    /**
     * The list of employees managed by this manager. This is a one-to-many relationship
     * where a manager can oversee multiple employees.
     */
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employee> employees;  // רשימה של עובדים שתחת המנהל

    @Column(name = "manager_bonus", nullable = false)
    private BigDecimal managerBonus = BigDecimal.ZERO;
}
