package com.dissertation.Meetings.dtos;

import com.dissertation.Meetings.entities.Employee;
import com.dissertation.Meetings.entities.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParticipantDto {

    private Response response;
    private Employee employee;

}
