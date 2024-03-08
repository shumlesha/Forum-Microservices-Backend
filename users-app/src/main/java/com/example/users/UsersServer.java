package com.example.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.users", "com.example.common", "com.example.auth"})
@EntityScan("com.example.common")
public class UsersServer {

	public static void main(String[] args) {
		SpringApplication.run(UsersServer.class, args);
	}

}
