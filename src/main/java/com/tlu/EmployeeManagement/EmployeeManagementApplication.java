package com.tlu.EmployeeManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
		System.out.print("Employee Management Application is running...");
		// /api/swagger-ui/index.html
		// mvn spring-boot:run
	}

}
