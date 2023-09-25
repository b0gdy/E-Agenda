package com.dissertation.Meetings.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusyTimeRegisterDto {

    private String id;
    private String date;
    private String start;
    private String end;
    private Long employeeId;
    private String description;

}
