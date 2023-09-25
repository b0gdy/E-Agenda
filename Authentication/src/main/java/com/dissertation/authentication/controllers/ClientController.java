package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.ClientDto;
import com.dissertation.authentication.dtos.ClientRegisterDto;
import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin
@Validated
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientRegisterDto clientRegisterDto) {

        ClientDto createdClient = clientService.create(clientRegisterDto);

        return ResponseEntity.created(URI.create("/clients" + createdClient.getId())).body(createdClient);

    }

    @RequestMapping(value = "/activate-account/{username}", method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> activateAccount(@PathVariable String username) {
        return ResponseEntity.ok().body(clientService.activateAccount(username));
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<ClientDto>> getAll() {

        return ResponseEntity.ok().body(clientService.getAll());

    }

    @GetMapping("/enabled")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<ClientDto>> getAllEnabled() {

        return ResponseEntity.ok().body(clientService.getAllEnabled());

    }

    @GetMapping("/get-by-company/{companyId}")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<List<ClientDto>> getByCompany(@PathVariable Long companyId) {

        return ResponseEntity.ok().body(clientService.getByCompany(companyId));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<ClientDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok().body(clientService.getById(id));

    }

    @GetMapping("/get-by-username")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<ClientDto> getByUsername(@RequestParam String username) {

        return ResponseEntity.ok().body(clientService.getByUsername(username));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client')")
    public ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) Long companyId, @RequestParam(required = false) String password) {

        return ResponseEntity.ok(clientService.update(id, firstName, lastName, companyId, password));

    }

    @PutMapping("/update-enabled/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client')")
    public ResponseEntity<ClientDto> updateEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {

        return ResponseEntity.ok(clientService.updateEnabled(id, enabled));

    }

    @PutMapping("/change-password/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client')")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestParam String password) {

        return ResponseEntity.ok(clientService.changePassword(id, password));

    }

    @PutMapping("/change-role/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> changeRole(@PathVariable Long id, @RequestParam String role) {

        return ResponseEntity.ok(clientService.changeRole(id, role));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        return ResponseEntity.ok(clientService.delete(id));

    }

}
