package com.dissertation.Meetings.mappers;

import com.dissertation.Meetings.dtos.ParticipantDto;
import com.dissertation.Meetings.entities.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(target = "response", source = "participant.response")
    ParticipantDto mapToDto(Participant participant);

    @Mapping(target = "response", source = "participantDto.response")
    Participant mapToEntity(ParticipantDto participantDto);

}
