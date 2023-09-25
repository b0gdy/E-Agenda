package com.dissertation.Tickets.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTokenDto {

    private String userName;
    private String password;
    private Set<RoleTokenDto> roles;

}
