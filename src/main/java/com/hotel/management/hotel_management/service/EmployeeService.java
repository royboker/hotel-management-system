package com.hotel.management.hotel_management.service;

import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.EmployeeResponse;
import com.hotel.management.hotel_management.payload.TaskDto;

import java.util.List;
/**
 * Interface for employee-related services in the hotel management system.
 * Provides methods for creating, retrieving, updating, and deleting employees,
 * as well as methods for employee-specific queries and tasks.
 */
public interface EmployeeService
{
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeResponse getAllEmployees(int pageNo, int pageSize, String sortBy, String sortDir);

    EmployeeDto getEmployeeById(Long id);

    EmployeeDto updateEmployee(EmployeeDto employeeDto,Long id);

    void deleteEmployeeById(Long id);

    List<EmployeeDto> getSalaryGreaterThan(double salary);

    List<EmployeeDto> getEmployeesByType(Type type);

    List<EmployeeDto> searchEmployees(String query);

    TaskDto getEmployeeTask(Long id);
}
