package com.dissertation.Meetings.services;

import com.dissertation.Meetings.dtos.UserTokenDto;
import com.dissertation.Meetings.entities.Employee;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name="Authentication")
public interface UserService {

    @GetMapping({"/getuserbytoken"})
    public UserTokenDto getByToken(@RequestParam String token, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

    @GetMapping({"/employees/{employeeId}"})
    public Employee getEmployeeById(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

}
