package com.dissertation.authentication.mappers;

import com.dissertation.authentication.dtos.ClientDto;
import com.dissertation.authentication.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", source = "client.id")
    @Mapping(target = "userName", source = "client.userName")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
//    @Mapping(target = "companyId", source = "client.companyId")
    ClientDto mapToDto(Client client);

    @Mapping(target = "id", source = "clientDto.id")
    @Mapping(target = "userName", source = "clientDto.userName")
    @Mapping(target = "firstName", source = "clientDto.firstName")
    @Mapping(target = "lastName", source = "clientDto.lastName")
//    @Mapping(target = "companyId", source = "clientDto.companyId")
    Client mapToEntity(ClientDto clientDto);

}
