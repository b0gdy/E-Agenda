package com.dissertation.Tickets.mappers;

import com.dissertation.Tickets.dtos.CompanyDto;
import com.dissertation.Tickets.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "id", source = "company.id")
    @Mapping(target = "name", source = "company.name")
    CompanyDto mapToDto(Company company);

    @Mapping(target = "id", source = "companyDto.id")
    @Mapping(target = "name", source = "companyDto.name")
    Company mapToEntity(CompanyDto companyDto);

}
