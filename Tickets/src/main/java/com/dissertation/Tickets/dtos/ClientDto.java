package com.dissertation.Tickets.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDto {

    private Long id;
    private String userName;
    private boolean enabled;
    private String role;
    private String firstName;
    private String lastName;
    private CompanyDto companyDto;

}
