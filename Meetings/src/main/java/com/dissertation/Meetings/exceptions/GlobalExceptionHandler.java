package com.dissertation.Meetings.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            BadRequestException.class,
            DisabledException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            RuntimeException.class,
            Exception.class,
            ExpiredJwtException.class,
            AuthenticationException.class,
            IOException.class,
            ServletException.class,
    })
    public ResponseEntity handle(Exception e) {

        return ResponseEntity.badRequest().body(e.getMessage());

    }

}
