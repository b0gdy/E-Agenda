package com.dissertation.Tickets.services;

import com.dissertation.Tickets.dtos.ClientDto;
import com.dissertation.Tickets.dtos.UserTokenDto;
import com.dissertation.Tickets.entities.Client;
import com.dissertation.Tickets.entities.Company;
import com.dissertation.Tickets.entities.Employee;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name="Authentication")
public interface UserService {

    @GetMapping({"/getuserbytoken"})
    public UserTokenDto getByToken(@RequestParam String token, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

    @GetMapping({"/employees/{employeeId}"})
    public Employee getEmployeeById(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

    @GetMapping({"/companies/{companyId}"})
    public Company getCompanyById(@PathVariable Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

    @GetMapping({"/clients/{clientId}"})
    public ClientDto getClientById(@PathVariable Long clientId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

    @GetMapping({"/clients/get-by-company-id/{companyId}"})
    public List<ClientDto> getClientByCompanyId(@PathVariable Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header);

}
