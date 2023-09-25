package com.dissertation.Meetings.mappers;

import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.entities.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "date", source = "meeting.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "meeting.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "meeting.end", dateFormat = "HH:mm")
    @Mapping(target = "createdAt", source = "meeting.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "meeting.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "participantDtos", source = "meeting.participants")
    MeetingDto mapToDto(Meeting meeting);

    @Mapping(target = "id", source = "meetingDto.id")
    @Mapping(target = "date", source = "meetingDto.date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "start", source = "meetingDto.start", dateFormat = "HH:mm")
    @Mapping(target = "end", source = "meetingDto.end", dateFormat = "HH:mm")
    @Mapping(target = "createdAt", source = "meetingDto.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "meetingDto.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "participants", source = "meetingDto.participantDtos")
    Meeting mapToEntity(MeetingDto meetingDto);

}
