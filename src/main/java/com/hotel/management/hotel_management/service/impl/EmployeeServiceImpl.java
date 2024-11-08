package com.hotel.management.hotel_management.service.impl;

import com.hotel.management.hotel_management.entity.Employee;
import com.hotel.management.hotel_management.entity.Manager;
import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.exception.APIException;
import com.hotel.management.hotel_management.exception.ResourceNotFoundException;
import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.EmployeeResponse;
import com.hotel.management.hotel_management.payload.TaskDto;
import com.hotel.management.hotel_management.repository.EmployeeRepository;
import com.hotel.management.hotel_management.repository.ManagerRepository;
import com.hotel.management.hotel_management.repository.TaskRepository;
import com.hotel.management.hotel_management.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing employees in the hotel management system.
 * Provides functionality for creating, updating, retrieving, and deleting employees,
 * as well as handling tasks and associations with managers.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService
{
    private EmployeeRepository employeeRepository;
    private TaskRepository taskRepository;
    private ManagerRepository managerRepository;
    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ManagerRepository managerRepository, TaskRepository taskRepository,
                               ModelMapper mapper,PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.taskRepository = taskRepository;
        this.mapper=mapper;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto)
    {
        Employee employee = this.mapToEntity(employeeDto);

        Employee newEmployee = employeeRepository.save(employee);

        EmployeeDto employeeResponce = this.mapToDto(newEmployee);

        return employeeResponce;
    }

    @Override
    public EmployeeResponse getAllEmployees(int pageNo, int pageSize, String sortBy, String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Employee> employees =  employeeRepository.findAll(pageable);


        List<Employee> employeeList = employees.getContent();
        ;

        List<EmployeeDto> content =  employeeList.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setContent(content);
        employeeResponse.setPageNo(employees.getNumber());
        employeeResponse.setPageSize(employees.getSize());
        employeeResponse.setTotalEmployee(employees.getTotalElements());
        employeeResponse.setTotalPages(employees.getTotalPages());
        employeeResponse.setLast(employees.isLast());

        return employeeResponse;

    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id",id));
        return mapToDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto, Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Employee","id",id));
        employee.setEmployeeBonus(employeeDto.getEmployeeBonus());
        employee.setEmployeeType(employeeDto.getEmployeeType());
        employee.setActive(employeeDto.isActive());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setDateOfBirth(employeeDto.getDateOfBirth());
        employee.setAge(employeeDto.getAge());
        employee.setSalary(employeeDto.getSalary());
        employee.setUsername(employeeDto.getUsername());
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        //employee.setRoles(employeeDto.getRoles());
        // check if there is a task to assign to the employee
        if (employeeDto.getTaskId() != null)
        {
            Task task = taskRepository.findById(employeeDto.getTaskId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Task" ,"id" ,employeeDto.getTaskId())
            );
            if(employee.getEmployeeType().equals(task.getTaskType()))//the Type of the employee and the task must be the same type
            {
                employee.setEmployeeTask(task);//assign the task to the employee
                task.setLastUpdate(LocalDateTime.now());//update the last update date
                employee.setActive(true);//set the employee to active after assign a task
                task.setTaskStatus(TaskStatus.IN_PROGRESS);//change the task status to in progress after assign the task to the employee
                task.setEmployee(employee);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST, "Employee Type and Task Type is not the matches");
            }
        }
        else
        {
            employee.setEmployeeTask(null);//if there is no task to set to the employee
        }

        // Checking if a manager ID exists and defining it.
        if (employeeDto.getManagerId() != null)
        {
            Manager manager = managerRepository.findById(employeeDto.getManagerId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Manager","id",employeeDto.getManagerId())
            );
            if(employee.getEmployeeType().equals(manager.getManagerType()))
            {
                manager.setUserId(employeeDto.getManagerId());
                employee.setManager(manager);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST,"Employee Type and Manager Type is not the matches");
            }
        }
        else
        {
            employee.setManager(null); // If no manager exists, we will leave it as null.
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id)
    {

        Employee employee = employeeRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Employee","id",id));
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDto> getSalaryGreaterThan(double salary) {
        List<Employee> employeesgetSalaryGreaterThan = employeeRepository.findBySalaryGreaterThan(salary);
        if (employeesgetSalaryGreaterThan.isEmpty())
        {
            throw new APIException(HttpStatus.NOT_FOUND,"There is no employees with greater salary then " +salary);
        }
        return  employeesgetSalaryGreaterThan.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getEmployeesByType(Type type) {
        List<Employee> employeesByType = employeeRepository.findByEmployeeType(type);
        if (employeesByType.isEmpty())
        {
            throw new APIException(HttpStatus.NOT_FOUND,"There is no " +type + " employees");
        }
        return employeesByType.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> searchEmployees(String query) {
        List<Employee> employees = employeeRepository.searchTasks(query);
        return employees.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());
    }

    @Override
    public TaskDto getEmployeeTask(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Employee","id",id));
        Task task = employee.getEmployeeTask();
        return mapToDto(task);


    }


    private EmployeeDto mapToDto(Employee employee) {

          EmployeeDto employeeDto = mapper.map(employee,EmployeeDto.class);
          employeeDto.setPassword(passwordEncoder.encode(employee.getPassword()));

//        // בדיקה אם לעובד יש משימה
//        if (employee.getEmployeeTask() != null) {
//            employeeDto.setTaskId(employee.getEmployeeTask().getTaskId());
//        }
//        else {
//            employeeDto.setTaskId(null);
//        }
//
//        // בדיקה אם קיים מנהל
//        if (employee.getManager() != null) {
//            employeeDto.setManagerId(employee.getManager().getUserId());
//        } else {
//            employeeDto.setManagerId(null); // אם אין מנהל, הגדר ל-null
//        }

        return employeeDto;
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {

          Employee employee = mapper.map(employeeDto,Employee.class);

        // check if there is a task to assign to the employee
        if (employeeDto.getTaskId() != null)
        {
            Task task = taskRepository.findById(employeeDto.getTaskId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Task" ,"id" ,employeeDto.getTaskId())
            );
            if(employee.getEmployeeType().equals(task.getTaskType()))//the Type of the employee and the task must be the same type
            {
                employee.setEmployeeTask(task);//assign the task to the employee
                task.setLastUpdate(LocalDateTime.now());//update the last update date
                employee.setActive(true);//set the employee to active after assign a task
                task.setTaskStatus(TaskStatus.IN_PROGRESS);//change the task status to in progress after assign the task to the employee
                task.setEmployee(employee);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST, "Employee Type and Task Type is not the matches");
            }
        }
        else
        {
            employee.setEmployeeTask(null);//if there is no task to set to the employee
        }

        // Checking if a manager ID exists and defining it.
        if (employeeDto.getManagerId() != null)
        {
            Manager manager = managerRepository.findById(employeeDto.getManagerId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Manager","id",employeeDto.getManagerId())
            );
            if(employee.getEmployeeType().equals(manager.getManagerType()))
            {
                manager.setUserId(employeeDto.getManagerId());
                employee.setManager(manager);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST,"Employee Type and Manager Type is not the matches");
            }
        }
        else
        {
            employee.setManager(null); // If no manager exists, we will leave it as null.
        }

        return employee;
    }
    //convert Entity to DTO
    private TaskDto mapToDto(Task task)
    {
        TaskDto taskDto = mapper.map(task,TaskDto.class);
        return taskDto;
    }

}
