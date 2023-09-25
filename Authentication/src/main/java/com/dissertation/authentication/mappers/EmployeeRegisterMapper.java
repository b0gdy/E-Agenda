package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.EmployeeRegisterDto;
import com.dissertation.authentication.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeRegisterMapper {

    @Mapping(target = "id", source = "employee.id")
    @Mapping(target = "userName", source = "employee.userName")
    @Mapping(target = "password", source = "employee.password")
    @Mapping(target = "firstName", source = "employee.firstName")
    @Mapping(target = "lastName", source = "employee.lastName")
    @Mapping(target = "position", source = "employee.position")
    @Mapping(target = "salary", source = "employee.salary")
    @Mapping(target = "hireDate", source = "employee.hireDate", dateFormat = "yyyy-MM-dd")
    EmployeeRegisterDto mapToDto(Employee employee);

    @Mapping(target = "id", source = "employeeRegisterDto.id")
    @Mapping(target = "userName", source = "employeeRegisterDto.userName")
    @Mapping(target = "password", source = "employeeRegisterDto.password")
    @Mapping(target = "firstName", source = "employeeRegisterDto.firstName")
    @Mapping(target = "lastName", source = "employeeRegisterDto.lastName")
    @Mapping(target = "position", source = "employeeRegisterDto.position")
    @Mapping(target = "salary", source = "employeeRegisterDto.salary")
    @Mapping(target = "hireDate", source = "employeeRegisterDto.hireDate", dateFormat = "yyyy-MM-dd")
    Employee mapToEntity(EmployeeRegisterDto employeeRegisterDto);

}
