package com.example.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.forum", "com.example.common"})
public class ForumServer {

	public static void main(String[] args) {
		SpringApplication.run(ForumServer.class, args);
	}

}
