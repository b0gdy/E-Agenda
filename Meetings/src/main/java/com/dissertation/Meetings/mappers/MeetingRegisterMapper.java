package com.dissertation.Meetings.mappers;

import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.dtos.MeetingRegisterDto;
import com.dissertation.Meetings.entities.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingRegisterMapper {

    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "date", source = "meeting.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "meeting.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "meeting.end", dateFormat = "HH:mm")
    @Mapping(target = "createdAt", source = "meeting.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "meeting.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    MeetingRegisterDto mapToDto(Meeting meeting);

    @Mapping(target = "id", source = "meetingRegisterDto.id")
    @Mapping(target = "date", source = "meetingRegisterDto.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "meetingRegisterDto.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "meetingRegisterDto.end", dateFormat = "HH:mm")
    @Mapping(target = "createdAt", source = "meetingRegisterDto.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "meetingRegisterDto.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    Meeting mapToEntity(MeetingRegisterDto meetingRegisterDto);

}
