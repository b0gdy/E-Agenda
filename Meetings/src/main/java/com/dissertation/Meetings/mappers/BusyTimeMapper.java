package com.dissertation.Meetings.mappers;

import com.dissertation.Meetings.dtos.BusyTimeDto;
import com.dissertation.Meetings.entities.BusyTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusyTimeMapper {

    @Mapping(target = "id", source = "busyTime.id")
    @Mapping(target = "date", source = "busyTime.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "busyTime.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "busyTime.end", dateFormat = "HH:mm")
//    @Mapping(target = "employee", source = "busyTime.employeeId")
    @Mapping(target = "description", source = "busyTime.description")
    BusyTimeDto mapToDto(BusyTime busyTime);

    @Mapping(target = "id", source = "busyTimeDto.id")
    @Mapping(target = "date", source = "busyTimeDto.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "busyTimeDto.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "busyTimeDto.end", dateFormat = "HH:mm")
//    @Mapping(target = "employeeId", source = "busyTimeDto.employee")
    @Mapping(target = "description", source = "busyTimeDto.description")
    BusyTime mapToEntity(BusyTimeDto busyTimeDto);


}
