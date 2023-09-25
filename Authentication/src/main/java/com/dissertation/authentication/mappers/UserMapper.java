package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.UserDto;
import com.dissertation.authentication.entities.Employee;
import com.dissertation.authentication.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    UserDto mapToDto(User user);

    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "userName", source = "userDto.userName")
    @Mapping(target = "enabled", source = "userDto.enabled")
    @Mapping(target = "firstName", source = "userDto.firstName")
    @Mapping(target = "lastName", source = "userDto.lastName")
    User mapToEntity(UserDto userDto);

}
