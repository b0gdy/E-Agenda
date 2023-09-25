package com.dissertation.authentication.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequestDto {

    private String userName;
    private String password;

}
