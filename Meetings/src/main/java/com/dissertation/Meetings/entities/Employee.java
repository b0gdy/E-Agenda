package com.dissertation.Meetings.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

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
