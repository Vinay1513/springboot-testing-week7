package com.example.TestingApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestcontainersConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>();
        container.start();
        return container;
    }
}

