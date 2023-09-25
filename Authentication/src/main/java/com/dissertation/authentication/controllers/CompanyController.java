package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.CompanyDto;
import com.dissertation.authentication.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/companies")
@CrossOrigin
@Validated
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyDto companyDto) {

        CompanyDto createdClientCompany = companyService.create(companyDto);

        return ResponseEntity.created(URI.create("/clientcompanies" + createdClientCompany.getId())).body(createdClientCompany);

    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<CompanyDto>> getAll() {

        return ResponseEntity.ok().body(companyService.getAll());

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<CompanyDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok().body(companyService.getById(id));

    }

    @GetMapping("/get-by-name")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<CompanyDto> getByName(@RequestParam String name) {

        return ResponseEntity.ok().body(companyService.getByName(name));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<CompanyDto> update(@PathVariable Long id, @RequestParam String name) {

        return ResponseEntity.ok(companyService.update(id, name));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        return ResponseEntity.ok(companyService.delete(id));

    }

}
