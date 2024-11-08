package com.hotel.management.hotel_management.payload;

import com.hotel.management.hotel_management.enums.Type;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for registering a new manager.
 * This class contains the necessary fields and validation for creating a manager record.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterManagerDto {


    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;


    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;


    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than or equal to 100")
    private int age;


    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dateOfBirth; // נשתמש במחרוזת לפורמט תאריך


    @Min(value = 0, message = "Salary must be positive")
    private double salary;


    @NotEmpty
    @Size(min = 5, max = 10, message = "Username must be between 5 and 10 characters")
    private String username;


    @NotEmpty
    @Size(min = 5, max = 10, message = "Password must be between 5 and 10 characters")
    private String password;


    @NotNull(message = "Manager type is required")
    private Type managerType;  // סוג המנהל (ניקיון, קבלה וכו')


    private List<Long> employeeIds;


    @Min(value = 0, message = "Bonus must be positive")
    private BigDecimal managerBonus = BigDecimal.ZERO;
}
