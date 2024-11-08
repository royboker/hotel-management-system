package com.hotel.management.hotel_management.repository;


import com.hotel.management.hotel_management.entity.Manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.List;


/**
 * Repository interface for performing CRUD and custom operations on Manager entities.
 * This interface extends JpaRepository and provides methods for querying Manager data.
 */
public interface ManagerRepository  extends JpaRepository<Manager,Long>
{
    boolean existsByUsername(String username);

    @Query("SELECT m FROM Manager m WHERE " +
            "m.username LIKE CONCAT('%',:query,'%') " +
            "OR m.firstName LIKE CONCAT('%',:query,'%')")
    List<Manager> searchTasks(String query);
}
