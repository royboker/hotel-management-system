package com.hotel.management.hotel_management.controller;

import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.EmployeeResponse;
import com.hotel.management.hotel_management.payload.TaskDto;
import com.hotel.management.hotel_management.service.EmployeeService;
import com.hotel.management.hotel_management.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(
        name = "CRUD REST APIs for Employee Resource"
)
public class EmployeeController
{
     private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Operation(
            summary = "Create Employee REST API",
            description = "Create a specific employee and adds to the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto)
    {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDto),HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get All Employees REST API",
            description = "Get all the employees from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping
    public EmployeeResponse getAllEmployees(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false)int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,//by amount of post
            @RequestParam(value ="sortBy",defaultValue = AppConstants.EMPLOYEE_DEFAULT_SORT_BY,required = false)String sortBy,//by value field
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIREACTION,required = false)String sortDir//by alphabet order
    )
    {
        return employeeService.getAllEmployees(pageNo,pageSize,sortBy,sortDir);
    }




    @Operation(
            summary = "Get Employee By Id REST API",
            description = "Get a specific employee by using id field from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(name ="id") Long id)
    {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }



    @Operation(
            summary = "Update Employee By Id REST API",
            description = "Update a specific employee fields by using id  from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@Valid@RequestBody EmployeeDto employeeDto,@PathVariable(name="id") Long id)
    {
        EmployeeDto employeeResponse = employeeService.updateEmployee(employeeDto,id);
        return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
    }



    @Operation(
            summary = "Delete Employee By Id REST API",
            description = "Delete a specific employee by using id from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name="id") Long id)
    {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>("Employee entity deleted successfully",HttpStatus.OK);
    }


    @Operation(
            summary = "Get Employees By Greater Salary REST API",
            description = "Get employees with greater salary thats bigger then the required field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/bysalary/{salary}")
    public ResponseEntity<List<EmployeeDto>> bySalary(@PathVariable(name="salary") double salary)
    {
        List<EmployeeDto> employeeByGreaterSalary = employeeService.getSalaryGreaterThan(salary);
        return ResponseEntity.ok(employeeByGreaterSalary);
    }


    @Operation(
            summary = "Get Employees By Employee Type REST API",
            description = "Get employees that have the same type has the required field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/bytype/{type}")
    public ResponseEntity<List<EmployeeDto>> byType(@PathVariable(name = "type") Type type)
    {
        List<EmployeeDto> employeesByType = employeeService.getEmployeesByType(type);
        return ResponseEntity.ok(employeesByType);

    }

    @Operation(
            summary = "Search Employee",
            description = "Search employee by enter value for username or first name"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> searchTasks(@RequestParam(name="query") String query)
    {
        return ResponseEntity.ok(employeeService.searchEmployees(query));
    }

    @Operation(
            summary = "Get Employee Task By Id REST API",
            description = "Get a the task that assign to specific employee by id field from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{id}/task")
    public ResponseEntity<TaskDto> getEmployeeTask(@PathVariable(name ="id") Long id)
    {
        return ResponseEntity.ok(employeeService.getEmployeeTask(id));
    }




}
