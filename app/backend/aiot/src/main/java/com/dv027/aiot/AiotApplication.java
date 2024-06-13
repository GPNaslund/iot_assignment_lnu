package com.dv027.aiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.dv027.aiot" })
public class AiotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiotApplication.class, args);
	}

}
