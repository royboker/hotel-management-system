package com.hotel.management.hotel_management.repository;

import com.hotel.management.hotel_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for performing CRUD operations on Role entities.
 * This interface extends JpaRepository and provides methods for querying Role data.
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
}
