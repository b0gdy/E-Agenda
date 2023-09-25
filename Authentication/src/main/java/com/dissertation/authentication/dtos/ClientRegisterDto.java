package com.dissertation.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientRegisterDto {

    private Long id;
    @NotNull(message = "Username cannot be null!")
    private String userName;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private Long companyId;

}
