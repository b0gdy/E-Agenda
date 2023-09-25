package com.dissertation.Meetings.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document("busytimes")
public class BusyTime {

    @Id
    private String id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Long employeeId;
    @Lob
    private String description;

}
