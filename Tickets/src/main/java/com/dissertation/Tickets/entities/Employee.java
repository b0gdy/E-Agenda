package com.dissertation.Tickets.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Employee {

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