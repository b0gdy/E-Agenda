package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.RoleDto;
import com.dissertation.authentication.entities.Employee;
import com.dissertation.authentication.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", source = "role.id")
    @Mapping(target = "name", source = "role.name")
    RoleDto mapToDto(Role role);

    @Mapping(target = "id", source = "roleDto.id")
    @Mapping(target = "name", source = "roleDto.name")
    Role mapToEntity(RoleDto roleDto);


}
