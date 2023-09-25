package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", source = "employee.id")
    @Mapping(target = "userName", source = "employee.userName")
    @Mapping(target = "enabled", source = "employee.enabled")
    @Mapping(target = "firstName", source = "employee.firstName")
    @Mapping(target = "lastName", source = "employee.lastName")
    @Mapping(target = "position", source = "employee.position")
    @Mapping(target = "salary", source = "employee.salary")
    @Mapping(target = "hireDate", source = "employee.hireDate", dateFormat = "yyyy-MM-dd")
    EmployeeDto mapToDto(Employee employee);

    @Mapping(target = "id", source = "employeeDto.id")
    @Mapping(target = "userName", source = "employeeDto.userName")
    @Mapping(target = "enabled", source = "employeeDto.enabled")
    @Mapping(target = "firstName", source = "employeeDto.firstName")
    @Mapping(target = "lastName", source = "employeeDto.lastName")
    @Mapping(target = "position", source = "employeeDto.position")
    @Mapping(target = "salary", source = "employeeDto.salary")
    @Mapping(target = "hireDate", source = "employeeDto.hireDate", dateFormat = "yyyy-MM-dd")
    Employee mapToEntity(EmployeeDto employeeDto);

}
