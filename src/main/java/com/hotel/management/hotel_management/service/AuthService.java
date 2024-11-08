package com.hotel.management.hotel_management.service;

import com.hotel.management.hotel_management.payload.LoginDto;
import com.hotel.management.hotel_management.payload.RegisterEmployeeDto;
import com.hotel.management.hotel_management.payload.RegisterManagerDto;
/**
 * Interface for authentication and registration services in the hotel management system.
 * Defines methods for user login and registration of employees and managers.
 */
public interface AuthService {
    String login(LoginDto loginDto);

    String registerEmployee(RegisterEmployeeDto registerEmployeeDto);

    String registerManager(RegisterManagerDto registerManagerDto);
}
