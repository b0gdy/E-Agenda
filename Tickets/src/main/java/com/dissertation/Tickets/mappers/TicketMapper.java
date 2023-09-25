package com.dissertation.Tickets.mappers;

import com.dissertation.Tickets.dtos.TicketDto;
import com.dissertation.Tickets.dtos.TicketRegisterDto;
import com.dissertation.Tickets.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "id", source = "ticket.id")
    @Mapping(target = "title", source = "ticket.title")
    @Mapping(target = "description", source = "ticket.description")
    @Mapping(target = "createdAt", source = "ticket.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "ticket.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "status", source = "ticket.status")
    @Mapping(target = "priority", source = "ticket.priority")
    @Mapping(target = "enabled", source = "ticket.enabled")
    TicketDto mapToDto(Ticket ticket);

    @Mapping(target = "id", source = "ticketDto.id")
    @Mapping(target = "title", source = "ticketDto.title")
    @Mapping(target = "description", source = "ticketDto.description")
    @Mapping(target = "createdAt", source = "ticketDto.createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "updatedAt", source = "ticketDto.updatedAt", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "status", source = "ticketDto.status")
    @Mapping(target = "priority", source = "ticketDto.priority")
    @Mapping(target = "enabled", source = "ticketDto.enabled")
    Ticket mapToEntity(TicketDto ticketDto);

}
