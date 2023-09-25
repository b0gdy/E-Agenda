package com.dissertation.Tickets.dtos;

import com.dissertation.Tickets.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketDto {

    private String id;
    private String title;
    private Description description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Employee employee;
    private Client client;
    private Status status;
    private Priority priority;
    private boolean enabled;

}
