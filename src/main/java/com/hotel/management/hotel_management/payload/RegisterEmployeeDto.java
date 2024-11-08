package com.hotel.management.hotel_management.payload;

import com.hotel.management.hotel_management.enums.Type;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for registering a new employee.
 * This class encapsulates the necessary fields and validation for creating an employee record.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEmployeeDto {


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

    @NotEmpty
    @Size(min = 5, max = 10, message = "Password must be between 5 and 10 characters")
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
