package com.dissertation.authentication.dtos;

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

    private Long id;
    private String userName;
    private String password;
    private Set<RoleTokenDto> roles;

}
