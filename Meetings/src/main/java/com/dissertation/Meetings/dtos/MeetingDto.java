package com.dissertation.Meetings.dtos;

import com.dissertation.Meetings.entities.Employee;
import com.dissertation.Meetings.entities.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MeetingDto {

    private String id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ParticipantDto> participantDtos;

}
