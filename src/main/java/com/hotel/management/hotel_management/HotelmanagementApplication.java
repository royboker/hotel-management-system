package com.hotel.management.hotel_management;

import com.hotel.management.hotel_management.entity.Role;
import com.hotel.management.hotel_management.entity.User;
import com.hotel.management.hotel_management.repository.RoleRepository;
import com.hotel.management.hotel_management.repository.UserRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Hotel Management App REST APIs",
				description = "Spring Boot Hotel Management App REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Roy",
						email = "royboker15@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)

		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Hotel Management App Documentation"
		)
)
public class HotelmanagementApplication implements CommandLineRunner
{

	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelmanagementApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;


	/**
	 * Initializes essential roles and an admin user for the application.
	 * This setup is intended for the initial run of the project to ensure that
	 * necessary roles (ADMIN, EMPLOYEE, MANAGER) and a default admin user are
	 * created in the database. If these entities already exist, they will not be duplicated.
	 *
	 * <p>Roles:
	 * <ul>
	 *   <li>ROLE_ADMIN - Used to grant administrator privileges.</li>
	 *   <li>ROLE_EMPLOYEE - Used for regular employee access.</li>
	 *   <li>ROLE_MANAGER - Used for managerial access.</li>
	 * </ul>
	 *
	 * <p>Admin User:
	 * <ul>
	 *   <li>Username: admin</li>
	 *   <li>Password: admin (encrypted)</li>
	 *   <li>Basic information and a default salary</li>
	 * </ul>
	 *
	 * <p>This function is executed once during the application startup.
	 * Re-running it will check for existing roles and the admin user to avoid duplication.</p>
	 *
	 * @throws Exception if an error occurs during initialization
	 */
	@Override
	public void run(String... args) throws Exception {
		//Check if roles exist in the database before attempting to add them
		if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
			Role adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
			roleRepository.save(adminRole);
		}

		if (roleRepository.findByName("ROLE_EMPLOYEE").isEmpty()) {
			Role employeeRole = new Role();
			employeeRole.setName("ROLE_EMPLOYEE");
			roleRepository.save(employeeRole);
		}

		if (roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
			Role managerRole = new Role();
			managerRole.setName("ROLE_MANAGER");
			roleRepository.save(managerRole);
		}

		// Check if the admin user exists before trying to add it again
		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setUsername("admin");

			// Use PasswordEncoder to encrypt the password
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			admin.setPassword(passwordEncoder.encode("admin"));

			admin.setDateOfBirth(LocalDate.now());
			admin.setAge(99);
			admin.setSalary(9999);

			// Add the ADMIN role to the user
			Set<Role> roleSet = new HashSet<>();
			roleSet.add(roleRepository.findByName("ROLE_ADMIN").get());
			admin.setRoles(roleSet);

			userRepository.save(admin);
		}
	}
}
