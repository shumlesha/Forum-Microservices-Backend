package com.example.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.example.forum"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.example.forum", "com.example.common", "com.example.securitylib"})
@EntityScan(basePackages = {"com.example.common.models","com.example.forum.models"})
@EnableJpaRepositories(basePackages = {"com.example.forum.repository"})
public class ForumServer {

	public static void main(String[] args) {
		SpringApplication.run(ForumServer.class, args);
	}

}
