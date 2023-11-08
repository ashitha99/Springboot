package com.employee.employee.Controller;

import com.employee.employee.Dto.EmployeeDto;
import com.employee.employee.Entity.Employee;
import com.employee.employee.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee/v1")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("addEmployee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto){
        try{
            employeeService.saveEmployee(employeeDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/employee/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id){
        return employeeService.findEmployeeById(id);
    }

    @PutMapping("/updateDetails")
    public ResponseEntity<?>updateProfile(@RequestBody EmployeeDto employeeDto){
        try{
            System.out.println("Updating the employee"+employeeDto);
            employeeService.updateProfile(employeeDto);
         return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

}





}
