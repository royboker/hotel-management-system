package com.hotel.management.hotel_management.repository;

import com.hotel.management.hotel_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for performing CRUD operations on User entities.
 * This interface extends JpaRepository and provides methods for querying User data.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
