package com.dissertation.Meetings.dtos;

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
public class MeetingRegisterDto {

    private String id;
    private String date;
    private String start;
    private String end;
    private String createdAt;
    private String updatedAt;
    private List<Long> participantIds;
    
}
