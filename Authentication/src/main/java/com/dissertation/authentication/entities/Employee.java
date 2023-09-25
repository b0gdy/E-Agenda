package com.dissertation.authentication.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@Entity
//@DiscriminatorValue("employee")
public class Employee extends User {

    private String position;
    private Integer salary;
    private LocalDate hireDate;

}
