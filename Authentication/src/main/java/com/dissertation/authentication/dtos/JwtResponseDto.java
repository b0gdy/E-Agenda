package com.dissertation.authentication.dtos;

import com.dissertation.authentication.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponseDto {

    private Long id;
    private String userName;
    private String role;
    private String jwtToken;

}
