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
public class CompanyDto {

    private Long id;
    @NotNull(message = "Company name cannot be null!")
    private String name;

}
