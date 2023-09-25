package com.dissertation.Meetings.dtos;

import com.dissertation.Meetings.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusyTimeDto {

    private String id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Employee employee;
    private String description;


}
