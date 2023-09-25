package com.dissertation.Tickets.controllers;

import com.dissertation.Tickets.dtos.TicketDto;
import com.dissertation.Tickets.dtos.TicketRegisterDto;
import com.dissertation.Tickets.entities.Description;
import com.dissertation.Tickets.entities.Priority;
import com.dissertation.Tickets.entities.Status;
import com.dissertation.Tickets.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@CrossOrigin
@Validated
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin') or hasRole('client')")
    public ResponseEntity<TicketDto> create(@Valid @RequestBody TicketRegisterDto ticketRegisterDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        TicketDto createdTicket = ticketService.create(ticketRegisterDto, header);

        return ResponseEntity.created(URI.create("/tickets" + createdTicket.getId())).body(createdTicket);

    }

    @GetMapping()
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getAll(header));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<TicketDto> getById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getById(id, header));

    }

    @GetMapping("/get-by-enabled")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getByEnabled(@RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByEnabled(enabled, header));

    }

    @GetMapping("/get-by-title/{title}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getByTitle(@PathVariable String title, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitle(title, header));

    }

    @GetMapping("/get-by-title-and-enabled/{title}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getByTitleAndEnabled(@PathVariable String title, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitleAndEnabled(title, enabled, header));

    }

    @GetMapping("/get-by-title-and-client")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByTitleAndClient(@RequestParam String title, @RequestParam Long clientId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitleAndClient(title, clientId, header));

    }

    @GetMapping("/get-by-title-and-client-and-enabled")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByTitleAndClientAndEnabled(@RequestParam String title, @RequestParam Long clientId, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitleAndClientAndEnabled(title, clientId, enabled, header));

    }

    @GetMapping("/get-by-title-and-company")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByTitleAndCompany(@RequestParam String title, @RequestParam Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitleAndCompany(title, companyId, header));

    }

    @GetMapping("/get-by-title-and-company-and-enabled")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByTitleAndCompanyAndEnabled(@RequestParam String title, @RequestParam Long companyId, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByTitleAndCompanyAndEnabled(title, companyId, enabled, header));

    }

    @GetMapping("/get-by-client/{clientId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByClient(@PathVariable Long clientId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByClient(clientId, header));

    }

    @GetMapping("/get-by-client-and-enabled/{clientId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByClientAndEnabled(@PathVariable Long clientId, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByClientAndEnabled(clientId, enabled, header));

    }

    @GetMapping("/get-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByEmployee(employeeId, header));

    }

    @GetMapping("/get-by-employee-and-enabled/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<TicketDto>> getByEmployeeAndEnabled(@PathVariable Long employeeId, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByEmployeeAndEnabled(employeeId, enabled, header));

    }

    @GetMapping("/get-by-company/{companyId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByCompany(@PathVariable Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByCompany(companyId, header));

    }

    @GetMapping("/get-by-company-and-enabled/{companyId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<List<TicketDto>> getByCompanyAndEnabled(@PathVariable Long companyId, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.getByCompanyAndEnabled(companyId, enabled, header));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
//    public ResponseEntity<TicketDto> update(@PathVariable String id, @RequestParam(required = false) String title, @RequestParam(required = false) Long employeeId, @RequestParam(required = false) Long clientId, @RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority, @RequestBody Description description, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
    public ResponseEntity<TicketDto> update(@PathVariable String id, @RequestBody TicketRegisterDto ticketRegisterDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

//        return ResponseEntity.ok(ticketService.update(id, title, employeeId, clientId, status, priority, description, header));
        return ResponseEntity.ok(ticketService.update(id, ticketRegisterDto, header));


    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<TicketDto> updateEnabled(@PathVariable String id, @RequestParam Status status, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok(ticketService.updateStatus(id, status, header));

    }

    @PutMapping("/update-enabled/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client')")
    public ResponseEntity<TicketDto> updateEnabled(@PathVariable String id, @RequestParam boolean enabled, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok(ticketService.updateEnabled(id, enabled, header));

    }

    @PutMapping("/update-employee/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<TicketDto> updateEmployee(@PathVariable String id, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok(ticketService.updateEmployee(id, employeeId, header));

    }

    @PutMapping("/add-description-details/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TicketDto> addDescriptionDetails(@PathVariable String id, @RequestBody Description description, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok(ticketService.addDescriptionDetails(id, description, header));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(ticketService.delete(id, header));

    }

}
