package com.dissertation.Tickets.services;

import com.dissertation.Tickets.dtos.ClientDto;
import com.dissertation.Tickets.dtos.TicketDto;
import com.dissertation.Tickets.dtos.TicketRegisterDto;
import com.dissertation.Tickets.entities.*;
import com.dissertation.Tickets.exceptions.ResourceNotFoundException;
import com.dissertation.Tickets.mappers.ClientMapper;
import com.dissertation.Tickets.mappers.CompanyMapper;
import com.dissertation.Tickets.mappers.TicketMapper;
import com.dissertation.Tickets.mappers.TicketRegisterMapper;
import com.dissertation.Tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketRegisterMapper ticketRegisterMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ClientMapper clientMapper;


//    @Autowired
//    private UserService userService;

    private Client mapClientDtoToClient(ClientDto clientDto) {

        Client client = clientMapper.mapToEntity(clientDto);
        client.setCompany(companyMapper.mapToEntity(clientDto.getCompanyDto()));

        return client;

    }

    private Employee getEmployeeById(Long employeeId, String header) {

        Employee employee = new Employee();
        try {
//            employee = userService.getEmployeeById(employeeId, header);
            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/employees/" + employeeId)
                    .baseUrl("authentication:8080/employees/" + employeeId)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, header)
                    .build();
            Mono<Employee> employeeMono = webClient.get().retrieve().bodyToMono(Employee.class);
            employee = employeeMono.block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " not found!");
        }

        return employee;

    }

    private Company getCompanyById(Long companyId, String header) {

        Company company = new Company();
        try {
//            company = userService.getCompanyById(companyId, header);
            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/companies/" + companyId)
                    .baseUrl("authentication:8080/companies/" + companyId)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, header)
                    .build();
            Mono<Company> companyMono = webClient.get().retrieve().bodyToMono(Company.class);
            company = companyMono.block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Company with id " + companyId + " not found!");
        }

        return company;

    }

    private Client getClientById(Long clientId, String header) {

        Client client = new Client();
        ClientDto clientDto = new ClientDto();
        try {
//            clientDto = userService.getClientById(clientId, header);
            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/clients/" + clientId)
                    .baseUrl("authentication:8080/clients/" + clientId)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, header)
                    .build();
            Mono<ClientDto> clientMono = webClient.get().retrieve().bodyToMono(ClientDto.class);
            clientDto = clientMono.block();
            client = mapClientDtoToClient(clientDto);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Client with id " + clientId + " not found!");
        }

        return client;

    }

    private List<Client> getClientByCompanyId(Long companyId, String header) {
        
        List<Client> clients = new ArrayList<>();
        List<ClientDto> clientDtos = new ArrayList<>();
        try {
//            clientDtos = userService.getClientByCompanyId(companyId, header);
            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/clients/get-by-company/" + companyId)
                    .baseUrl("authentication:8080/clients/get-by-company/" + companyId)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, header)
                    .build();
            Flux<ClientDto> clientsFlux = webClient.get().retrieve().bodyToFlux(ClientDto.class);
            clientDtos = clientsFlux.collectList().block();
            for(ClientDto c : clientDtos) {
                Client client = mapClientDtoToClient(c);
                clients.add(client);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Company with id " + companyId + " not found!");
        }

        return clients;

    }

    private Ticket mapTicketDtoToTicket(TicketDto ticketDto) {

        Ticket ticket = ticketMapper.mapToEntity(ticketDto);
        if(ticket.getEmployeeId() != 0) {
            ticket.setEmployeeId(ticketDto.getEmployee().getId());
        }
        ticket.setClientId(ticketDto.getClient().getId());

        return ticket;

    }

    private TicketDto mapTicketToTicketDto(Ticket ticket, String header) {

        TicketDto ticketDto = ticketMapper.mapToDto(ticket);
        if(ticket.getEmployeeId() != 0) {
            ticketDto.setEmployee(getEmployeeById(ticket.getEmployeeId(), header));
        }
        ticketDto.setClient(getClientById(ticket.getClientId(), header));

        return ticketDto;

    }

    public TicketDto create(TicketRegisterDto ticketRegisterDto, String header) {

        Ticket ticket = ticketRegisterMapper.mapToEntity(ticketRegisterDto);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setEnabled(true);
        ticket.setStatus(Status.Working);

        ticketRepository.save(ticket);
        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    public List<TicketDto> getAll(String header) {

        List<Ticket> tickets = ticketRepository.findAllByOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc();
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public TicketDto getById(String id, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();
        TicketDto ticketDto = mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    public List<TicketDto> getByEnabled(boolean enabled, String header) {

        List<Ticket> tickets = ticketRepository.findByEnabledOrderByStatusDescPriorityAscCreatedAtAsc(enabled);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitle(String title, String header) {

        List<Ticket> tickets = new ArrayList<>();
        if(title.equals("no_title")) {
            tickets = ticketRepository.findAllByOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc();
        } else {
            tickets = ticketRepository.findByTitleLikeOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title);
        }
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitleAndEnabled(String title, boolean enabled, String header) {

        List<Ticket> tickets = new ArrayList<>();
        if(title.equals("no_title")) {
            tickets = ticketRepository.findByEnabledOrderByStatusDescPriorityAscCreatedAtAsc(enabled);
        } else {
            tickets = ticketRepository.findByTitleLikeAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, enabled);
        }
//        List<Ticket> tickets = ticketRepository.findByTitleLikeAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, enabled);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitleAndClient(String title, Long clientId, String header) {

        Client client = getClientById(clientId, header);
        List<Ticket> tickets = ticketRepository.findByTitleLikeAndClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, clientId);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitleAndClientAndEnabled(String title, Long clientId, boolean enabled, String header) {

        Client client = getClientById(clientId, header);
        List<Ticket> tickets = ticketRepository.findByTitleLikeAndClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, clientId, enabled);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitleAndCompany(String title, Long companyId, String header) {

        Company company = getCompanyById(companyId, header);
        List<Client> clients = getClientByCompanyId(companyId, header);

        List<Ticket> tickets = new ArrayList<>();
        for(Client c : clients) {
            List<Ticket> ts = ticketRepository.findByTitleLikeAndClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, c.getId());
            tickets.addAll(ts);
        }

        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByTitleAndCompanyAndEnabled(String title, Long companyId, boolean enabled, String header) {

        Company company = getCompanyById(companyId, header);
        List<Client> clients = getClientByCompanyId(companyId, header);

        List<Ticket> tickets = new ArrayList<>();
        for(Client c : clients) {
            List<Ticket> ts = ticketRepository.findByTitleLikeAndClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(title, c.getId(), enabled);
            tickets.addAll(ts);
        }

        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }
    
    public List<TicketDto> getByClient(Long clientId, String header) {

        Client client = getClientById(clientId, header);
        List<Ticket> tickets = ticketRepository.findByClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(clientId);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByClientAndEnabled(Long clientId, boolean enabled, String header) {

        Client client = getClientById(clientId, header);
        List<Ticket> tickets = ticketRepository.findByClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(clientId, enabled);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByEmployee(Long employeeId, String header) {

        if(employeeId != 0) {
            Employee employee = getEmployeeById(employeeId, header);
        }
        List<Ticket> tickets = ticketRepository.findByEmployeeIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(employeeId);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByEmployeeAndEnabled(Long employeeId, boolean enabled, String header) {

        if(employeeId != 0) {
            Employee employee = getEmployeeById(employeeId, header);
        }
        List<Ticket> tickets = ticketRepository.findByEmployeeIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(employeeId, enabled);
        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByCompany(Long companyId, String header) {

        Company company = getCompanyById(companyId, header);
        List<Client> clients = getClientByCompanyId(companyId, header);

        List<Ticket> tickets = new ArrayList<>();
        for(Client c : clients) {
            List<Ticket> ts = ticketRepository.findByClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(c.getId());
            tickets.addAll(ts);
        }

        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    public List<TicketDto> getByCompanyAndEnabled(Long companyId, boolean enabled, String header) {

        Company company = getCompanyById(companyId, header);
        List<Client> clients = getClientByCompanyId(companyId, header);

        List<Ticket> tickets = new ArrayList<>();
        for(Client c : clients) {
            List<Ticket> ts = ticketRepository.findByClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(c.getId(), enabled);
            tickets.addAll(ts);
        }

        List<TicketDto> ticketDtos = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = mapTicketToTicketDto(t, header);
            ticketDtos.add(ticketDto);
        }

        return ticketDtos;

    }

    @Transactional
//    public TicketDto update(String id, String title, Long employeeId, Long clientId, Status status, Priority priority, Description description, String header) {
    public TicketDto update(String id, TicketRegisterDto ticketRegisterDto, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ticket.setUpdatedAt(LocalDateTime.now());

//        if(title != null) {
//            ticket.setTitle(title);
//        }
//
//        if(employeeId != null) {
//            Employee employee = getEmployeeById(employeeId, header);
//            ticket.setEmployeeId(employeeId);
//        }
//
//        if(clientId != null) {
//            Client client = getClientById(clientId, header);
//            ticket.setClientId(clientId);
//        }
//
//        if(status != null) {
//            ticket.setStatus(status);
//        }
//
//        if(priority != null) {
//            ticket.setPriority(priority);
//        }
//
//        if(description.getDetails() != null) {
//            ticket.setDescription(description);
//        }

        if(ticketRegisterDto.getTitle() != null) {
            ticket.setTitle(ticketRegisterDto.getTitle() );
        }

        if(ticketRegisterDto.getEmployeeId() != null) {
            Employee employee = getEmployeeById(ticketRegisterDto.getEmployeeId(), header);
            ticket.setEmployeeId(ticketRegisterDto.getEmployeeId());
        }

        if(ticketRegisterDto.getClientId() != null) {
            Client client = getClientById(ticketRegisterDto.getClientId(), header);
            ticket.setClientId(ticketRegisterDto.getClientId());
        }

        if(ticketRegisterDto.getStatus() != null) {
            ticket.setStatus(ticketRegisterDto.getStatus());
        }

        if(ticketRegisterDto.getPriority() != null) {
            ticket.setPriority(ticketRegisterDto.getPriority());
        }

        if(ticketRegisterDto.getDescription() != null) {
            ticket.setDescription(ticketRegisterDto.getDescription());
        }

        ticketRepository.save(ticket);
        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    @Transactional
    public TicketDto updateStatus(String id, Status status, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(status);
        ticketRepository.save(ticket);

        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    @Transactional
    public TicketDto updateEnabled(String id, boolean enabled, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setEnabled(enabled);
        ticketRepository.save(ticket);

        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    @Transactional
    public TicketDto updateEmployee(String id, Long employeeId, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();
        ticket.setUpdatedAt(LocalDateTime.now());

        Employee employee = new Employee();
        if((employeeId != null) && (employeeId != 0)) {
            employee = getEmployeeById(employeeId, header);
            ticket.setEmployeeId(employeeId);
        } else if (employeeId == 0) {
            ticket.setEmployeeId(employeeId);
        }

        ticketRepository.save(ticket);

//        if(employeeId != 0) {
//            return "Ticket assigned to employee " + employee.getFirstName() + " " + employee.getLastName() + "!";
//        } else {
//            return "Ticket unassigned!";
//        }

        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    @Transactional
    public TicketDto addDescriptionDetails(String id, Description description, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();
        ticket.setUpdatedAt(LocalDateTime.now());

        if(description != null) {
            if(ticket.getDescription() != null) {
                List<String> details = ticket.getDescription().getDetails();
                List<String> details2 = description.getDetails();
                details.addAll(details2);
                ticket.getDescription().setDetails(details);
            } else {
                ticket.setDescription(description);
            }
        }

        ticketRepository.save(ticket);
        TicketDto ticketDto =  mapTicketToTicketDto(ticket, header);

        return ticketDto;

    }

    @Transactional
    public String delete(String id, String header) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + id + " not found!");
        }
        Ticket ticket = ticketOptional.get();

        ticketRepository.deleteById(id);

        return "Ticket with id " + id + " deleted!";

    }

}
