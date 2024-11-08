package com.hotel.management.hotel_management.security;

import com.hotel.management.hotel_management.entity.User;
import com.hotel.management.hotel_management.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
/**
 * Custom implementation of the UserDetailsService interface for loading user-specific data.
 * This class fetches user details from the database using the UserRepository and maps user roles
 * to Spring Security authorities for authentication and authorization.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found with this username"+username));

        Set<GrantedAuthority> authorities = user.getRoles().stream().map((role -> new
                SimpleGrantedAuthority(role.getName()))).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),authorities);


    }
}

