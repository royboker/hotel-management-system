package com.hotel.management.hotel_management.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
/**
 * Represents the data transfer object for user login in the hotel management system.
 * This class is used to encapsulate the necessary information for user authentication.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto
{
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}
