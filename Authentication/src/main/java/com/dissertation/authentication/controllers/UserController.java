package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.ClientDto;
import com.dissertation.authentication.dtos.UserDto;
import com.dissertation.authentication.dtos.UserTokenDto;
import com.dissertation.authentication.entities.User;
import com.dissertation.authentication.services.JwtService;
import com.dissertation.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }



    @GetMapping()
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<List<UserDto>> getAll() {

        return ResponseEntity.ok().body(userService.getAll());

    }

    @GetMapping("/enabled")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<List<UserDto>> getAllEnabled() {

        return ResponseEntity.ok().body(userService.getAllEnabled());

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok().body(userService.getById(id));

    }

    @GetMapping("/get-by-username")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<UserDto> getByUsername(@RequestParam String username) {

        return ResponseEntity.ok().body(userService.getByUsername(username));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('client') or hasRole('employee')")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String password) {

        return ResponseEntity.ok(userService.update(id, firstName, lastName, password));

    }
    
    
    
    @PostMapping({"/user/register"})
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping({"/user/getbyusername/{username}"})
    public User register(@PathVariable String username) {
        return userService.findByUserName(username);
    }







    @GetMapping({"/user/forAdmin"})
    @PreAuthorize("hasRole('admin')")
    public String forAdmin() {
        return "This URL is only accessible to admin!";
    }

    @GetMapping({"/user/forUser"})
    @PreAuthorize("hasRole('user')")
    public String forUser() {
        return "This URL is only accessible to user!";
    }
    
}
