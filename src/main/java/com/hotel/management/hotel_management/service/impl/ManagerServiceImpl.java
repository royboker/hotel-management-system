package com.hotel.management.hotel_management.service.impl;

import com.hotel.management.hotel_management.entity.Employee;
import com.hotel.management.hotel_management.entity.Manager;
import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.exception.APIException;
import com.hotel.management.hotel_management.exception.ResourceNotFoundException;
import com.hotel.management.hotel_management.payload.EmployeeDto;
import com.hotel.management.hotel_management.payload.ManagerDto;
import com.hotel.management.hotel_management.payload.ManagerResponse;
import com.hotel.management.hotel_management.repository.EmployeeRepository;
import com.hotel.management.hotel_management.repository.ManagerRepository;
import com.hotel.management.hotel_management.repository.TaskRepository;
import com.hotel.management.hotel_management.service.ManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Service implementation for managing managers in the hotel management system.
 * Provides functionality for creating, updating, retrieving, and deleting managers,
 * handling their employees and tasks, and associating managers with employees.
 */
@Service
public class ManagerServiceImpl implements ManagerService {


    private ManagerRepository managerRepository;
    private EmployeeRepository employeeRepository;
    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;
    private TaskRepository taskRepository;

    @Autowired
    public ManagerServiceImpl(EmployeeRepository employeeRepository, ManagerRepository managerRepository,
                              ModelMapper mapper,PasswordEncoder passwordEncoder,TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.mapper=mapper;
        this.passwordEncoder=passwordEncoder;
        this.taskRepository=taskRepository;
    }

    @Override
    public ManagerDto createManager(ManagerDto managerDto) {

        Manager manager = this.mapToEntity(managerDto);

        Manager newManager = managerRepository.save(manager);

        ManagerDto managerResponce = this.mapToDto(newManager);

        return managerResponce;
    }

    @Override
    public ManagerResponse getAllManagers(int pageNo, int pageSize, String sortBy, String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Manager> managers =  managerRepository.findAll(pageable);


        List<Manager> managerList = managers.getContent();


        List<ManagerDto> content =  managerList.stream().map(manager -> mapToDto(manager)).collect(Collectors.toList());

        ManagerResponse managerResponse = new ManagerResponse();
        managerResponse.setContent(content);
        managerResponse.setPageNo(managers.getNumber());
        managerResponse.setPageSize(managers.getSize());
        managerResponse.setTotalManagers(managers.getTotalElements());
        managerResponse.setTotalPages(managers.getTotalPages());
        managerResponse.setLast(managers.isLast());

        return managerResponse;

    }

    @Override
    public ManagerDto getManagerById(Long id) {
        Manager manager = managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager","id",id));
        return mapToDto(manager);
    }

    @Override
    public ManagerDto updateManagerById(ManagerDto managerDto, Long id) {
        Manager manager = managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager","id",id));
        manager.setManagerBonus(managerDto.getManagerBonus());
        manager.setManagerType(manager.getManagerType());
        manager.setAge(managerDto.getAge());
        manager.setFirstName(managerDto.getFirstName());
        manager.setLastName(managerDto.getLastName());
        manager.setUsername(managerDto.getUsername());
        manager.setDateOfBirth(managerDto.getDateOfBirth());
        manager.setSalary(managerDto.getSalary());
        manager.setPassword(managerDto.getPassword());
       // manager.setRole(managerDto.getRole());

        // Updating the employee list.
        if (managerDto.getEmployeeIds() != null && !managerDto.getEmployeeIds().isEmpty()) {
            // Retrieving employees from the database by their IDs.
            List<Employee> newEmployees = employeeRepository.findAllById(managerDto.getEmployeeIds());

            // Checking the manager's type.
            Type managerType = manager.getManagerType();


            // Verifying that all employees are of the same type as the manager.
            boolean allMatch = newEmployees.stream().allMatch(employee -> employee.getEmployeeType().equals(managerType));

            if (allMatch)
            {
                // Removing the old employees from their previous manager.
                for (Employee employee : manager.getEmployees()) {
                    if (!newEmployees.contains(employee)) {
                        employee.setManager(null); // Removing the manager from the previous employees.
                    }
                }

                // Adding the new manager to the employees.
                for (Employee employee : newEmployees) {
                    // If an employee already has another manager, we will remove them from the previous manager.
                    if (employee.getManager() != null && !employee.getManager().equals(manager)) {
                        employee.getManager().getEmployees().remove(employee);
                    }
                    employee.setManager(manager); // Updating the new manager for the employee.
                }

                // Updating the employee list in the manager.
                manager.setEmployees(newEmployees);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST, "Not all employees match the manager's type");
            }
        }
        else
        {
            // If there is no employee list, we will clear the manager's employee list.
            for (Employee employee : manager.getEmployees())
            {
                employee.setManager(null); // Removing the manager from the existing employees.
            }
            manager.setEmployees(new ArrayList<>());
        }

        // Saving the updated manager in the database.
        Manager updatedManager = managerRepository.save(manager);

        // Returning the updated ManagerDto.
        return mapToDto(updatedManager);


    }

    @Override
    public void deleteManagerById(Long id) {
        Manager manager = managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager","id",id));

        // If the manager has employees, we will release all employees from their association with him.
        if (manager.getEmployees() != null && !manager.getEmployees().isEmpty()) {
            for (Employee employee : manager.getEmployees()) {
                employee.setManager(null); // Removing the manager from the employee.
                employeeRepository.save(employee); // Updating the employee in the database.
            }
        }

        managerRepository.delete(manager);
    }

    @Override
    public List<EmployeeDto> getEmployeesByManagerId(Long managerId) {
        //Find the manager by the ID.
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));

        // Retrieve the manager's employee list.
        List<Employee> employees = manager.getEmployees();

        if( employees.isEmpty())
        {
            throw new APIException(HttpStatus.NOT_FOUND,"This Manager " + manager.getUserId() + " not have employess");
        }
        // Converting the employee list to DTO.
        return employees.stream().map(this::mapEmployeeToDto).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId, Long managerId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->
                new ResourceNotFoundException("Employee","id",employeeId)
        );

        Manager manager = managerRepository.findById(managerId).orElseThrow(
                ()-> new ResourceNotFoundException("Manager","id",managerId)
        );

        if(!(employee.getManager().getUserId().equals(manager.getUserId())))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee does not belong to the manager");
        }
        return mapEmployeeToDto(employee);
    }

    @Override
    public EmployeeDto updateEmployeeByManager(Long managerId, Long employeeId, EmployeeDto employeeDto) {
        //retrieve employee entity by id
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->
                new ResourceNotFoundException("Employee","id",employeeId)
        );
        //retrieve manager entity by id
        Manager manager = managerRepository.findById(managerId).orElseThrow(
                ()-> new ResourceNotFoundException("Manager","id",managerId)
        );
        //if the employee does not belong to the manager
        if(!(employee.getManager().getUserId().equals(manager.getUserId())))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee does not belong to the manager");
        }

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
        // Defining a task if its ID exists.
        if (employeeDto.getTaskId() != null)
        {
            Task task = taskRepository.findById(employeeDto.getTaskId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Task" ,"id" ,employeeDto.getTaskId())
            );
            if(employee.getEmployeeType().equals(task.getTaskType()))//the Type of the employee and the task must be the same type
            {
               // task.setTaskId(employeeDto.getTaskId()); // update task id
                employee.setEmployeeTask(task);//set the task to the employee
                task.setLastUpdate(LocalDateTime.now());//update the last update date
                employee.setActive(true);//set the employee to active after set a task
                task.setTaskStatus(TaskStatus.IN_PROGRESS);//change the task status to in progress after set the task to the employee
                task.setEmployee(employee);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST, "Employee Type and Task Type is not the matches");
            }
        }
        else
        {
            employee.setEmployeeTask(null);
        }


        Employee updatedEmployee = employeeRepository.save(employee);

        return mapEmployeeToDto(updatedEmployee);


    }

    @Override
    public void deleteEmployeeByManager(Long employeeId, Long managerId)
    {
        //retrieve employee entity by id
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->
                new ResourceNotFoundException("Employee","id",employeeId)
        );
        //retrieve manager entity by id
        Manager manager = managerRepository.findById(managerId).orElseThrow(
                ()-> new ResourceNotFoundException("Manager","id",managerId)
        );
        //if the employee does not belong to the manager
        if(!(employee.getManager().getUserId().equals(manager.getUserId())))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee does not belong to the manager");
        }

        if(employee.getEmployeeTask()!=null) {
            Task task = employee.getEmployeeTask();
            task.setEmployee(null);
            task.setTaskStatus(TaskStatus.NOT_STARTED);
            task.setLastUpdate(LocalDateTime.now());
            taskRepository.save(task);
        }
        employeeRepository.delete(employee);


    }

    @Override
    public List<ManagerDto> searchManagers(String query) {
        List<Manager> managers = managerRepository.searchTasks(query);
        return managers.stream().map(manager -> mapToDto(manager)).collect(Collectors.toList());
    }

    @Override
    public void addTaskToEmployeeByManager(Long managerId, Long employeeId, Long taskId) {
        //retrieve employee entity by id
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->
                new ResourceNotFoundException("Employee","id",employeeId)
        );
        //retrieve manager entity by id
        Manager manager = managerRepository.findById(managerId).orElseThrow(
                ()-> new ResourceNotFoundException("Manager","id",managerId)
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                ()-> new ResourceNotFoundException("Task" ,"id" ,taskId)
        );
        //if the employee does not belong to the manager
        if(!(employee.getManager().getUserId().equals(manager.getUserId())))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee does not belong to the manager");
        }
        if(!(employee.getEmployeeType().equals(task.getTaskType())))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee does not have the same type has the task");
        }
        if(task.getTaskStatus().equals(TaskStatus.IN_PROGRESS) || task.getEmployee()!=null)
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Task is already in progress by other employee");
        }

        employee.setEmployeeTask(task);//set the task to the employee
        task.setLastUpdate(LocalDateTime.now());//update the last update date
        employee.setActive(true);//set the employee to active after set a task
        task.setTaskStatus(TaskStatus.IN_PROGRESS);//change the task status to in progress after set the task to the employee
        task.setEmployee(employee);

        employeeRepository.save(employee);
        taskRepository.save(task);



    }

    // Mapping from Entity to DTO.Dto
    private ManagerDto mapToDto(Manager manager) {
        ManagerDto managerDto = mapper.map(manager,ManagerDto.class);
          managerDto.setPassword(passwordEncoder.encode(manager.getPassword()));

        // If the manager has employees, we will add the list of employee IDs.
        if (manager.getEmployees() != null) {
            List<Long> employeeIds = manager.getEmployees().stream()
                    .map(Employee::getUserId)
                    .collect(Collectors.toList());
            managerDto.setEmployeeIds(employeeIds);
        }

        return managerDto;
    }

    //Mapping from DTO to Entity.
    private Manager mapToEntity(ManagerDto managerDto) {

        Manager manager = mapper.map(managerDto,Manager.class);

        // Handling the employee list if it is not null.
        if (managerDto.getEmployeeIds() != null && !managerDto.getEmployeeIds().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(managerDto.getEmployeeIds());
            // Checking the type of the manager.
            Type managerType = manager.getManagerType();


            // Verifying that all employees are of the same type as the manager.
            boolean allMatch = employees.stream().allMatch(employee -> employee.getEmployeeType().equals(managerType));

            if (allMatch) {
                manager.setEmployees(employees);
                // Updating all employees whose manager is the new manager.
                employees.forEach(employee -> employee.setManager(manager));
            } else {
                throw new APIException(HttpStatus.BAD_REQUEST, "Not all employees match the manager's type");
            }
        }

        return manager;
    }


    //Function for mapping Employee to EmployeeDto.
    private EmployeeDto mapEmployeeToDto(Employee employee) {
        EmployeeDto employeeDto = mapper.map(employee,EmployeeDto.class);
        employeeDto.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeDto;
    }

}

