package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.EmployeeRegisterDto;
import com.dissertation.authentication.dtos.JwtRequestDto;
import com.dissertation.authentication.dtos.JwtResponseDto;
import com.dissertation.authentication.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok().body(employeeService.getById(id));

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeRegisterDto employeeRegisterDto) {

        EmployeeDto createdEmployee = employeeService.create(employeeRegisterDto);

        return ResponseEntity.created(URI.create("/employees" + createdEmployee.getId())).body(createdEmployee);

    }

    @RequestMapping(value = "/activate-account/{username}", method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> activateAccount(@PathVariable String username) {
        return ResponseEntity.ok().body(employeeService.activateAccount(username));
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<EmployeeDto>> getAll() {

        return ResponseEntity.ok().body(employeeService.getAll());

    }

    @GetMapping("/enabled")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<EmployeeDto>> getAllEnabled() {

        return ResponseEntity.ok().body(employeeService.getAllEnabled());

    }

//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('admin') or hasRole('employee')")
//    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
//
//        return ResponseEntity.ok().body(employeeService.getById(id));
//
//    }

    @GetMapping("/get-by-username")
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<EmployeeDto> getByUsername(@RequestParam String username) {

        return ResponseEntity.ok().body(employeeService.getByUsername(username));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String position, @RequestParam(required = false) Integer salary, @RequestParam(required = false) String role, @RequestParam(required = false) String password) {

        return ResponseEntity.ok(employeeService.update(id, firstName, lastName, position, salary, role, password));

    }

    @PutMapping("/update-enabled/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<EmployeeDto> updateEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {

        return ResponseEntity.ok(employeeService.updateEnabled(id, enabled));

    }

    @PutMapping("/change-password/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestParam String password) {

        return ResponseEntity.ok(employeeService.changePassword(id, password));

    }

    @PutMapping("/change-role/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> changeRole(@PathVariable Long id, @RequestParam String role) {

        return ResponseEntity.ok(employeeService.changeRole(id, role));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        return ResponseEntity.ok(employeeService.delete(id));

    }
    
}
