package com.hotel.management.hotel_management.controller;

import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.ManagerDto;
import com.hotel.management.hotel_management.payload.ManagerResponse;
import com.hotel.management.hotel_management.service.ManagerService;
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
@RequestMapping("/api/managers")
@Tag(
        name = "CRUD REST APIs for Manager Resource"
)
public class ManagerController
{
    private ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(
            summary = "Create Manager REST API",
            description = "Create a specific manager and adds to the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ManagerDto> createManager(@Valid @RequestBody ManagerDto managerDto)
    {
        return new ResponseEntity<>(managerService.createManager(managerDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Managers REST API",
            description = "Get all the managers from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ManagerResponse getAllManagers(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false)int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,//by amount of post
            @RequestParam(value ="sortBy",defaultValue = AppConstants.MANAGER_DEFAULT_SORT_BY,required = false)String sortBy,//by value field
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIREACTION,required = false)String sortDir//by alphabet order
    )
    {
        return managerService.getAllManagers(pageNo,pageSize,sortBy,sortDir);
    }

    @Operation(
            summary = "Get Manager By Id REST API",
            description = "Get a specific manager by using id field from the database"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ManagerDto> getManagerById(@PathVariable(name="id") Long id)
    {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    @Operation(
            summary = "Update Manager By Id REST API",
            description = "Update a specific manager fields by using id  from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ManagerDto> updateManagerById(@Valid@RequestBody ManagerDto managerDto ,
                                                        @PathVariable(name="id") Long id )
    {
        ManagerDto managerResponce = managerService.updateManagerById(managerDto,id);
        return new ResponseEntity<>(managerResponce,HttpStatus.OK);
    }


    @Operation(
            summary = "Delete Manager By Id REST API",
            description = "Delete a specific manager by using id from the data base"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable(name="id") Long id)
    {
        managerService.deleteManagerById(id);
        return new ResponseEntity<>("Manager Entity deleted successfully",HttpStatus.OK);
    }


    @Operation(
            summary = "Get All Manager's Employees REST API",
            description = "Get all the employees under a specific manager using id field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{managerId}/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByManagerId(@PathVariable Long managerId) {
        List<EmployeeDto> employees = managerService.getEmployeesByManagerId(managerId);
        return ResponseEntity.ok(employees);
    }


    @Operation(
            summary = "Get Manager's Employee By Id REST API",
            description = "Get a specific employee under a specific manager using id field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{managerId}/employees/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name ="managerId") Long managerId)
    {
        EmployeeDto employeeDto = managerService.getEmployeeById(employeeId,managerId);
        return new ResponseEntity<>(employeeDto,HttpStatus.OK);
    }

    @Operation(
            summary = "Update Manager's Employee By Id REST API",
            description = "Update a specific employee under a specific manager using id field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{managerId}/employees/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployeeByManger(
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name ="managerId") Long managerId,
            @Valid@RequestBody EmployeeDto employeeDto)
    {
        EmployeeDto employeeDto1 = managerService.updateEmployeeByManager(managerId,employeeId,employeeDto);
        return new ResponseEntity<>(employeeDto1,HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Manager's Employee By Id REST API",
            description = "Delete a specific employee under a specific manager using id field"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{managerId}/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployeeByManager(
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name ="managerId") Long managerId)
    {
        managerService.deleteEmployeeByManager(employeeId,managerId);
        return new ResponseEntity<>("Employee Entity deleted successfully",HttpStatus.OK);
    }

    @Operation(
            summary = "Search Manager",
            description = "Search Manager by enter value for username or first name"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ManagerDto>> searchTasks(@RequestParam(name="query") String query)
    {
        return ResponseEntity.ok(managerService.searchManagers(query));
    }


    @Operation(
            summary = "Add a Task Manager's Employee by task id REST API ",
            description = "Add a specific task to a specific employee by his manager"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{managerId}/employees/{employeeId}/task/{taskId}")
    public ResponseEntity<String> addTaskToEmployeeByManager(
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name ="managerId") Long managerId,
            @PathVariable(name="taskId") Long taskId
    )
    {
        managerService.addTaskToEmployeeByManager(managerId,employeeId,taskId);
        return ResponseEntity.ok("Task assign successful to the employee " + employeeId + " by manager " +managerId);
    }

}
