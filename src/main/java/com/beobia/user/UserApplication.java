package com.beobia.user;

import com.beobia.user.entity.Role;
import com.beobia.user.entity.User;
import com.beobia.user.service.UserService;
import com.beobia.user.utils.JwtUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtUtil jwtUtil(){ return new JwtUtil();}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role("ROLE_USER"));
			userService.saveRole(new Role("ROLE_MANAGER"));
			userService.saveRole(new Role("ROLE_ADMIN"));
			userService.saveRole(new Role("ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(
					"bobcat1",
					"bobby",
					"abc",
					"bob123@hotmail.com",
					"password"
			));
			userService.saveUser(new User(
					"johnnyboy",
					"johhny",
					"man",
					"jb@gmail.com",
					"password"
			));
			userService.saveUser(new User(
					"MachoManRandySavage",
					"Robert",
					"Langdon",
					"robert_langdon@geocities.com",
					"password"
			));
			userService.saveUser(new User(
					"Hot_Showers",
					"Jamie",
					"Oliver",
					"notthecook@gmail.com",
					"password"
			));

			userService.assignRoleToUser("bobcat1", "ROLE_USER");
			userService.assignRoleToUser("bobcat1", "ROLE_ADMIN");
			userService.assignRoleToUser("bobcat1", "ROLE_MANAGER");
			userService.assignRoleToUser("johnnyboy", "ROLE_USER");
			userService.assignRoleToUser("MachoManRandySavage", "ROLE_USER");
			userService.assignRoleToUser("Hot_Showers", "ROLE_USER");
		};
	}
}
