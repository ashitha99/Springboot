package com.employee.employee.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;

}
