package com.dissertation.Meetings.mappers;

import com.dissertation.Meetings.dtos.BusyTimeRegisterDto;
import com.dissertation.Meetings.entities.BusyTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusyTimeRegisterMapper {

    @Mapping(target = "id", source = "busyTime.id")
    @Mapping(target = "date", source = "busyTime.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "busyTime.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "busyTime.end", dateFormat = "HH:mm")
    @Mapping(target = "employeeId", source = "busyTime.employeeId")
    @Mapping(target = "description", source = "busyTime.description")
    BusyTimeRegisterDto mapToDto(BusyTime busyTime);

    @Mapping(target = "id", source = "busyTimeRegisterDto.id")
    @Mapping(target = "date", source = "busyTimeRegisterDto.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "busyTimeRegisterDto.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "busyTimeRegisterDto.end", dateFormat = "HH:mm")
    @Mapping(target = "employeeId", source = "busyTimeRegisterDto.employeeId")
    @Mapping(target = "description", source = "busyTimeRegisterDto.description")
    BusyTime mapToEntity(BusyTimeRegisterDto busyTimeRegisterDto);
    
}
