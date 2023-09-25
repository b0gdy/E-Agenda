package com.dissertation.authentication.controllers;

import com.dissertation.authentication.dtos.JwtRequestDto;
import com.dissertation.authentication.dtos.JwtResponseDto;
import com.dissertation.authentication.dtos.UserTokenDto;
import com.dissertation.authentication.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.JavaVersion;
import org.springframework.core.SpringVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Security;

@RestController
@CrossOrigin
@Validated
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/login"})
//    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto jwtRequestDto) throws Exception {
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto jwtRequestDto) {
        return ResponseEntity.ok().body(jwtService.createJwtToken(jwtRequestDto));
    }


    @PutMapping({"/disableuser/{id}"})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> disableUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(jwtService.disableUser(id));
    }

    @GetMapping  ({"/getuserbytoken"})
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<UserTokenDto> getUserByToken (@RequestParam String token) {
        return ResponseEntity.ok().body(jwtService.getUserByToken(token));
    }

    @GetMapping  ({"/getuserbyusername"})
    @PreAuthorize("hasRole('admin') or hasRole('employee') or hasRole('client')")
    public ResponseEntity<UserTokenDto> getUserByUserName (@RequestParam String userName) {
        return ResponseEntity.ok().body(jwtService.getUserByUserName(userName));
    }




    @GetMapping({"/get-spring-version"})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> getSpringVersion() {
        var response = "spring version = " + SpringVersion.getVersion() + "\n" + "jdk version = " + System.getProperty("java.version") + "\n" + "java version = " + JavaVersion.getJavaVersion().toString();
        System.out.println("spring version = " + SpringVersion.getVersion());
        System.out.println("jdk version = " + System.getProperty("java.version"));
        System.out.println("java version = " + JavaVersion.getJavaVersion().toString());
        System.out.println("security version = " + Security.getProperty("security.version"));
        return ResponseEntity.ok().body(response);
    }

}
