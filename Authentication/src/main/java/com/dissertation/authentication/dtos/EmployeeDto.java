package com.dissertation.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDto {

    private Long id;
    private String userName;
    private boolean enabled;
    private String role;
    private String firstName;
    private String lastName;
    private String position;
    private Integer salary;
    private LocalDate hireDate;

}
