package com.dissertation.authentication.configuration;

import com.dissertation.authentication.entities.Employee;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
@ComponentScan(basePackageClasses = Employee.class)
public class Config {

    @Bean
    public Employee getEmployee() {
        return new Employee();
    }

}


