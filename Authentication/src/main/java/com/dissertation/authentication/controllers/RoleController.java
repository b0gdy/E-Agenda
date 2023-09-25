package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.EmployeeRegisterDto;
import com.dissertation.authentication.dtos.RoleDto;
import com.dissertation.authentication.entities.Role;
import com.dissertation.authentication.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping({"/create"})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RoleDto> create(@Valid @RequestBody Role role) {

        RoleDto createdRole = roleService.create(role);

        return ResponseEntity.created(URI.create("/employees" + createdRole.getId())).body(createdRole);

    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<RoleDto>> getAll() {

        return ResponseEntity.ok().body(roleService.getAll());

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok().body(roleService.getById(id));

    }

    @GetMapping("/getbyname/{name}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RoleDto> getByName(@PathVariable String name) {

        return ResponseEntity.ok().body(roleService.getByName(name));

    }

}
