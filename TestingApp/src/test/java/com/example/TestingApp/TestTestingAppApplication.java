package com.example.TestingApp;

import org.springframework.boot.SpringApplication;

public class TestTestingAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestingAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
