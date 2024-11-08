package com.hotel.management.hotel_management.service;

import com.hotel.management.hotel_management.entity.Employee;
import com.hotel.management.hotel_management.entity.Manager;
import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.ManagerDto;
import com.hotel.management.hotel_management.payload.ManagerResponse;

import java.util.List;
/**
 * Interface for manager-related services in the hotel management system.
 * Provides methods for creating, retrieving, updating, and deleting managers,
 * as well as managing employees under specific managers and handling tasks.
 */
public interface ManagerService
{
    ManagerDto createManager(ManagerDto managerDto);

     ManagerResponse getAllManagers(int pageNo, int pageSize, String sortBy, String sortDir);

    ManagerDto getManagerById(Long id);

    ManagerDto updateManagerById(ManagerDto managerDto ,Long id);

    void deleteManagerById(Long id);

    List<EmployeeDto> getEmployeesByManagerId(Long managerId);

    EmployeeDto getEmployeeById(Long employeeId,Long managerId);

    EmployeeDto updateEmployeeByManager(Long managerId,Long employeeId,EmployeeDto employeeDto);

    void deleteEmployeeByManager(Long employeeId,Long managerId);

    List<ManagerDto> searchManagers(String query);

    void addTaskToEmployeeByManager(Long managerId,Long employeeId,Long taskId);

}
