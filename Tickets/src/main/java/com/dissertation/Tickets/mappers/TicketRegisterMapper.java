package com.dissertation.Tickets.mappers;

import com.dissertation.Tickets.dtos.TicketRegisterDto;
import com.dissertation.Tickets.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketRegisterMapper {

    @Mapping(target = "id", source = "ticket.id")
    @Mapping(target = "title", source = "ticket.title")
    @Mapping(target = "description", source = "ticket.description")
    @Mapping(target = "createdAt", source = "ticket.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "ticket.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "employeeId", source = "ticket.employeeId")
    @Mapping(target = "clientId", source = "ticket.clientId")
    @Mapping(target = "status", source = "ticket.status")
    @Mapping(target = "priority", source = "ticket.priority")
    @Mapping(target = "enabled", source = "ticket.enabled")
    TicketRegisterDto mapToDto(Ticket ticket);

    @Mapping(target = "id", source = "ticketRegisterDto.id")
    @Mapping(target = "title", source = "ticketRegisterDto.title")
    @Mapping(target = "description", source = "ticketRegisterDto.description")
    @Mapping(target = "createdAt", source = "ticketRegisterDto.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "ticketRegisterDto.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "employeeId", source = "ticketRegisterDto.employeeId")
    @Mapping(target = "clientId", source = "ticketRegisterDto.clientId")
    @Mapping(target = "status", source = "ticketRegisterDto.status")
    @Mapping(target = "priority", source = "ticketRegisterDto.priority")
    @Mapping(target = "enabled", source = "ticketRegisterDto.enabled")
    Ticket mapToEntity(TicketRegisterDto ticketRegisterDto);

}
