package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmInterfaceApplication.class, args);
		
		 // Initialize ATM and start the application
        ATM atm = new ATM();
        atm.start();
	}

}
