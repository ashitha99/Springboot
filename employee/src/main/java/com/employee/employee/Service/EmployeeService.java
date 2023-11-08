package com.employee.employee.Service;

import com.employee.employee.Dto.EmployeeDto;
import com.employee.employee.Entity.Employee;
import com.employee.employee.Repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee=new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmailId(employeeDto.getEmailId());
        employeeRepository.save(employee);


    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void updateProfile(EmployeeDto employeeDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeDto.getId());

        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();

            // Update the relevant fields of the existing employee with the values from the DTO
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmailId(employeeDto.getEmailId());
            // Set other fields as needed

            // Save the updated employee back to the database
            employeeRepository.save(existingEmployee);
        } else {
            throw new EntityNotFoundException("Employee not found");
        }



    }

}
