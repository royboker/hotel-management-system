package com.hotel.management.hotel_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a user in the hotel management system. This class serves as the base class
 * for all user types, such as employees and managers, and includes common properties and
 * relationships related to users.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age" , nullable = false)
    private int age;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name="salary" , nullable = false)
    private double salary;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;


    /**
     * The set of roles assigned to the user. This is a many-to-many relationship,
     * where a user can have multiple roles.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
        joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "userId"),
        inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

}
