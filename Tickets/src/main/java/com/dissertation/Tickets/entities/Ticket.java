package com.dissertation.Tickets.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document("tickets")
public class Ticket {

    @Id
    private String id;
    private String title;
    private Description description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long employeeId;
    private long clientId;
    private Status status;
    private Priority priority;
    private boolean enabled;

}
