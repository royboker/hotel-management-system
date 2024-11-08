package com.hotel.management.hotel_management.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotel.management.hotel_management.enums.Type;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for representing employee data in the hotel management system.
 * This class is used for transferring data between client and server in a structured way.
 */
@Data

public class EmployeeDto
{

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @NotEmpty
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be no more than 100")
    private int age;

    @NotNull(message = "Date of birth is required")
    // מומלץ לשים לב לפורמט התאריך בעבודה עם LocalDate
    private LocalDate dateOfBirth;

    @Min(value = 0, message = "Salary must be positive")
    private double salary;

    @NotEmpty
    @Size(min = 5, max = 10, message = "Username must be between 5 and 10 characters")
    private String username;

    private String password;

    @Min(value = 0, message = "Bonus must be positive")
    private BigDecimal employeeBonus = BigDecimal.ZERO;

    private Long taskId;

    @NotNull(message = "Employee type is required")
    private Type employeeType;

    @NotNull(message = "Active status is required true or false")
    private boolean isActive;

    private Long managerId;
}
