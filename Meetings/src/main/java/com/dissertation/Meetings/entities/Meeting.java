package com.dissertation.Meetings.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Document("meetings")
public class Meeting {

    @Id
    private String id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Participant> participants;

}
