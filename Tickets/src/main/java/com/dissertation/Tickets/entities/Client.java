package com.dissertation.Tickets.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {

    private Long id;
    private String userName;
    private boolean enabled;
    private String role;
    private String firstName;
    private String lastName;
    private Company company;

}
