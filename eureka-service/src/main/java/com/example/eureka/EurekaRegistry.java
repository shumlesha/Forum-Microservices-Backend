package com.example.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationPropertiesScan(basePackages = {"com.example.eureka"})
@SpringBootApplication
@EnableEurekaServer
public class EurekaRegistry {

	public static void main(String[] args) {
		SpringApplication.run(EurekaRegistry.class, args);
	}

}
