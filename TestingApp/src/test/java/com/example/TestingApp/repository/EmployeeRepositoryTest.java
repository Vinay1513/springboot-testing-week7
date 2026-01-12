package com.example.TestingApp.repository;

import com.example.TestingApp.entities.Employee;
import com.example.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .name("vinay")
                .email("vinay@gmail.com")
                .salary(21L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee() {

        employeeRepository.save(employee);

        List<Employee> employeeList =
                employeeRepository.findByEmail(employee.getEmail());

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(1);
        assertThat(employeeList.get(0).getEmail())
                .isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotValid_thenReturnNull() {

        String email = "demo@gmail.com";

        List<Employee> employeeList =
                employeeRepository.findByEmail(employee.getEmail());
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }
}
