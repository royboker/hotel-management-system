package com.hotel.management.hotel_management.repository;

import com.hotel.management.hotel_management.entity.Employee;
import com.hotel.management.hotel_management.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for performing CRUD and custom operations on Employee entities.
 * This interface extends JpaRepository and provides various methods to retrieve employees
 * based on different criteria.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long>
{
    // מציאת עובדים לפי משכורת מעל ערך מסוים
    List<Employee> findBySalaryGreaterThan(Double salary);

    //מציאת עובדים לפי סוג
    List<Employee> findByEmployeeType(Type type);

    boolean existsByUsername(String username);

    ///////////Not yet executed, to be done later.
    // מציאת כל העובדים הפעילים (isActive = true)
    List<Employee> findByIsActiveTrue();

    // מציאת כל העובדים הלא פעילים (isActive = false)
    List<Employee> findByIsActiveFalse();

    // מציאת עובדים לפי משכורת מתחת לערך מסוים
    List<Employee> findBySalaryLessThan(double salary);

    // מציאת עובדים לפי בונוס מעל סכום מסוים
    List<Employee> findByEmployeeBonusGreaterThan(BigDecimal bonus);

    // מציאת עובדים לפי שם פרטי
    List<Employee> findByFirstName(String firstName);

    // מציאת עובדים לפי שם משפחה
    List<Employee> findByLastName(String lastName);

    // מציאת עובדים שנולדו לפני תאריך מסוים
    List<Employee> findByDateOfBirthBefore(LocalDate date);

    // מציאת עובדים שנולדו אחרי תאריך מסוים
    List<Employee> findByDateOfBirthAfter(LocalDate date);

    // מציאת עובדים לפי סוג משימה משויך
    List<Employee> findByEmployeeTaskTaskType(Type taskType);

    @Query("SELECT e FROM Employee e WHERE " +
            "e.username LIKE CONCAT('%',:query,'%') " +
            "OR e.firstName LIKE CONCAT('%',:query,'%')")
    List<Employee> searchTasks(String query);
}
