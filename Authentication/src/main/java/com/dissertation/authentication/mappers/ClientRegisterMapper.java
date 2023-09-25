package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.ClientRegisterDto;
import com.dissertation.authentication.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientRegisterMapper {

    @Mapping(target = "id", source = "client.id")
    @Mapping(target = "userName", source = "client.userName")
    @Mapping(target = "password", source = "client.password")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    @Mapping(target = "companyId", source = "client.companyId")
    ClientRegisterDto mapToDto(Client client);

    @Mapping(target = "id", source = "clientRegisterDto.id")
    @Mapping(target = "userName", source = "clientRegisterDto.userName")
    @Mapping(target = "password", source = "clientRegisterDto.password")
    @Mapping(target = "firstName", source = "clientRegisterDto.firstName")
    @Mapping(target = "lastName", source = "clientRegisterDto.lastName")
    @Mapping(target = "companyId", source = "clientRegisterDto.companyId")
    Client mapToEntity(ClientRegisterDto clientRegisterDto);

}
