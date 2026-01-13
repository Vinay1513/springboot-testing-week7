package com.example.TestingApp.servicess;

import com.example.TestingApp.dto.EmployeeDto;
import com.example.TestingApp.entities.Employee;
import com.example.TestingApp.exceptions.ResourceNotFoundException;
import com.example.TestingApp.repositories.EmployeeRepository;
import com.example.TestingApp.services.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
     private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee mockemployee;
    private EmployeeDto mockemployeeDto;

    @BeforeEach
    void setUp() {
        mockemployee = Employee.builder()
                .id(1L).
                email("vinay@gmail.com").
                name("Vinay").
                salary(200L).
                build();
mockemployeeDto = modelMapper.map(mockemployee, EmployeeDto.class);

    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {

        // assign
     Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockemployee));

        EmployeeDto employeeDto = employeeServiceImpl.getEmployeeById(id);
        //assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockemployee.getEmail());
        verify(employeeRepository,only()).findById(id);

    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsNotPresent_ThenReturnEmptyEmployeeDto() {
        Long id = 1L;
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeServiceImpl.getEmployeeById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + id);

    verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeDoesNotExist_ThenReturnEmployeeDto() {

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(List.of());

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(mockemployee);

        EmployeeDto result = employeeServiceImpl.createNewEmployee(mockemployeeDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(mockemployee.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeAlreadyExists_ThenThrowException() {

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(List.of(mockemployee));

        assertThatThrownBy(() ->
                employeeServiceImpl.createNewEmployee(mockemployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee already exists");

        verify(employeeRepository, never()).save(any());
    }

}
