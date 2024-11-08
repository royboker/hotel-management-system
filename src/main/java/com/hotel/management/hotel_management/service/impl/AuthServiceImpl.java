package com.hotel.management.hotel_management.service.impl;

import com.hotel.management.hotel_management.entity.Employee;
import com.hotel.management.hotel_management.entity.Manager;
import com.hotel.management.hotel_management.entity.Role;
import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.exception.APIException;
import com.hotel.management.hotel_management.exception.ResourceNotFoundException;
import com.hotel.management.hotel_management.payload.LoginDto;
import com.hotel.management.hotel_management.payload.RegisterEmployeeDto;
import com.hotel.management.hotel_management.payload.RegisterManagerDto;
import com.hotel.management.hotel_management.repository.*;
import com.hotel.management.hotel_management.security.JwtTokenProvider;
import com.hotel.management.hotel_management.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Implementation of the authentication and registration service for the hotel management system.
 * This service handles user login, employee registration, and manager registration with various validation checks.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ManagerRepository managerRepository;
    private TaskRepository taskRepository;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, EmployeeRepository employeeRepository, AuthenticationManager authenticationManager,ManagerRepository managerRepository,
                           JwtTokenProvider jwtTokenProvider,TaskRepository taskRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.authenticationManager = authenticationManager;
        this.managerRepository=managerRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.taskRepository=taskRepository;
    }

    @Override
    public String login(LoginDto loginDto) {


         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                 loginDto.getUsername(),loginDto.getPassword() ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
         
        return token;
    }

    @Override
    public String registerEmployee(RegisterEmployeeDto registerEmployeeDto) {

        //check for username exists in database
        if(employeeRepository.existsByUsername(registerEmployeeDto.getUsername()))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Username is already exists!");
        }

        Employee employee = new Employee();
        employee.setFirstName(registerEmployeeDto.getFirstName());
        employee.setLastName(registerEmployeeDto.getLastName());
        employee.setAge(registerEmployeeDto.getAge());
        employee.setDateOfBirth(registerEmployeeDto.getDateOfBirth());
        employee.setSalary(registerEmployeeDto.getSalary());
        employee.setUsername(registerEmployeeDto.getUsername());
        employee.setPassword(passwordEncoder.encode(registerEmployeeDto.getPassword()));
        employee.setEmployeeBonus(registerEmployeeDto.getEmployeeBonus());
        employee.setEmployeeType(registerEmployeeDto.getEmployeeType());
        employee.setActive(false);

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE").get();
        roleSet.add(userRole);
        employee.setRoles(roleSet);


        // check if there is a task to assign to the employee
        if (registerEmployeeDto.getTaskId() != null)
        {
            Task task = taskRepository.findById(registerEmployeeDto.getTaskId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Task" ,"id" ,registerEmployeeDto.getTaskId())
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

        // Checking if a manager ID exists and its definition.
        if (registerEmployeeDto.getManagerId() != null)
        {
            Manager manager = managerRepository.findById(registerEmployeeDto.getManagerId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Manager","id",registerEmployeeDto.getManagerId())
            );
            if(employee.getEmployeeType().equals(manager.getManagerType()))
            {
                manager.setUserId(registerEmployeeDto.getManagerId());
                employee.setManager(manager);
            }
            else
            {
                throw new APIException(HttpStatus.BAD_REQUEST,"Employee Type and Manager Type is not the matches");
            }
        }
        else
        {
            employee.setManager(null); // If a manager does not exist, we will leave it as null.
        }

        employeeRepository.save(employee);

        return "Employee register successfully!";

    }

    @Override
    public String registerManager(RegisterManagerDto registerManagerDto) {
        if(managerRepository.existsByUsername(registerManagerDto.getUsername()))
        {
            throw new APIException(HttpStatus.BAD_REQUEST,"Username is already exists!");
        }

        Manager manager = new Manager();
        manager.setFirstName(registerManagerDto.getFirstName());
        manager.setLastName(registerManagerDto.getLastName());
        manager.setAge(registerManagerDto.getAge());
        manager.setDateOfBirth(registerManagerDto.getDateOfBirth());
        manager.setSalary(registerManagerDto.getSalary());
        manager.setManagerBonus(registerManagerDto.getManagerBonus());
        manager.setManagerType(registerManagerDto.getManagerType());
        manager.setUsername(registerManagerDto.getUsername());
        manager.setPassword(passwordEncoder.encode(registerManagerDto.getPassword()));

        // Handling the employee list if it is not null.
        if (registerManagerDto.getEmployeeIds() != null && !registerManagerDto.getEmployeeIds().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(registerManagerDto.getEmployeeIds());
            manager.setEmployees(employees);
            // לעדכן גם את כל העובדים שהמנהל שלהם הוא המנהל החדש
            employees.forEach(employee -> employee.setManager(manager));
        }


        Set<Role> roleSet = new HashSet<>();
        Role managerRole = roleRepository.findByName("ROLE_MANAGER").get();
        roleSet.add(managerRole);
        manager.setRoles(roleSet);

        // Updating the employee list.
        if (registerManagerDto.getEmployeeIds() != null && !registerManagerDto.getEmployeeIds().isEmpty()) {
            // Retrieving employees from the database by IDs.
            List<Employee> newEmployees = employeeRepository.findAllById(registerManagerDto.getEmployeeIds());

            // Checking the type of the manager.
            Type managerType = manager.getManagerType();


            //Verifying that all employees are of the same type as the manager.
            boolean allMatch = newEmployees.stream().allMatch(employee -> employee.getEmployeeType().equals(managerType));

            if (allMatch)
            {
                // Removing the old employees from their previous manager.
                for (Employee employee : manager.getEmployees()) {
                    if (!newEmployees.contains(employee)) {
                        employee.setManager(null); // הסרת המנהל מהעובדים הקודמים
                    }
                }

                // Adding the new manager to the employees.
                for (Employee employee : newEmployees) {
                    //If an employee already has another manager, we will remove them from the previous manager.
                    if (employee.getManager() != null && !employee.getManager().equals(manager)) {
                        employee.getManager().getEmployees().remove(employee);
                    }
                    employee.setManager(manager); // Updating the new manager for the employee.
                }

                //Updating the employee list in the manager.
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
        managerRepository.save(manager);

        return "Manager register successfully!";

    }
}
