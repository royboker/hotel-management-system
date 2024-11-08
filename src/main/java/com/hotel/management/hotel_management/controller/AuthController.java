package com.hotel.management.hotel_management.controller;

import com.hotel.management.hotel_management.payload.JWTAuthResponse;
import com.hotel.management.hotel_management.payload.LoginDto;
import com.hotel.management.hotel_management.payload.RegisterEmployeeDto;
import com.hotel.management.hotel_management.payload.RegisterManagerDto;
import com.hotel.management.hotel_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Handler;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "CRUD REST APIs for Auth Resource"
)
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(
            summary = "Login REST API",
            description = "Login form by username and password to get a bearer token"
    )
    @PostMapping(value = {"/login"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto)
    {
       String token =  authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

       return ResponseEntity.ok(jwtAuthResponse);
    }

    @Operation(
            summary = "Employee Register REST API",
            description = "Employee register form to add new employee entity to the database"
    )
    @PostMapping(value = {"/register/employee"})
    public ResponseEntity<String> employeeRegister(@RequestBody RegisterEmployeeDto registerEmployeeDto)
    {
        String response = authService.registerEmployee(registerEmployeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Manager Register REST API",
            description = "Manager register form to add new manager entity to the database"
    )
    @PostMapping(value = {"/register/manager"})
    public ResponseEntity<String> managerRegister(@RequestBody RegisterManagerDto registerManagerDto)
    {
        String response = authService.registerManager(registerManagerDto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


}
