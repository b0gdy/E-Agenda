package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.EmployeeRegisterDto;
import com.dissertation.authentication.entities.ActivationLink;
import com.dissertation.authentication.entities.Email;
import com.dissertation.authentication.entities.Employee;
import com.dissertation.authentication.entities.Role;
import com.dissertation.authentication.exceptions.BadRequestException;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.mappers.EmployeeMapper;
import com.dissertation.authentication.mappers.EmployeeRegisterMapper;
import com.dissertation.authentication.repositories.ActivationLinkRepository;
import com.dissertation.authentication.repositories.EmployeeRepository;
import com.dissertation.authentication.repositories.RoleRepository;
import com.dissertation.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRegisterMapper employeeRegisterMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private ActivationLinkRepository activationLinkRepository;

    public String getEncodedPassword(String password) {

        return passwordEncoder.encode(password);

    }

    public EmployeeDto create(EmployeeRegisterDto employeeRegisterDto) {

        Employee employee = employeeRegisterMapper.mapToEntity(employeeRegisterDto);

        String userName = employeeRegisterDto.getUserName();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee e : employees) {
            if(e.getUserName().equals(userName)) {
                throw new BadRequestException("Employee with username " + userName + " already exists!");
            }
        }

        String password = employeeRegisterDto.getPassword();
        employee.setPassword(getEncodedPassword(password));

//        employee.setEnabled(true);
        employee.setEnabled(false);

        String roleName = employeeRegisterDto.getRole();
        Role role = roleRepository.findByName(roleName).get();
        Set<Role> employeeRoles = new HashSet<>();
        employeeRoles.add(role);
        employee.setRoles(employeeRoles);

        LocalDate hireDate = LocalDate.now();
        employee.setHireDate(hireDate);

        employeeRepository.save(employee);
        EmployeeDto employeeDto = employeeMapper.mapToDto(employee);
        employeeDto.setRole(employee.getRoles().iterator().next().getName());



        if(employeeDto.getUserName().equals("employee744@gmail.com")) {

            try {

                Email email = new Email();

                SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setFrom(sender);

                email.setRecipient(employeeDto.getUserName());
                mailMessage.setTo(email.getRecipient());

                email.setSubject("Activate your account!");
                mailMessage.setSubject(email.getSubject());

//                String link = "http://localhost:4200/account-activated/" + employeeDto.getUserName();
                String link = "http://localhost:8080/employees/activate-account/" + employeeDto.getUserName();
                ActivationLink activationLink = new ActivationLink();
                activationLink.setLink(link);
                activationLinkRepository.save(activationLink);

                String body = "";
                body = body + "Hey!" + "\n" + "Please click the following link to activate your account!\n\n" +
                        "Username: " + employeeDto.getUserName() + "\n" +
                        "Firstname: " + employeeDto.getFirstName() + "\n" +
                        "Lastname: " + employeeDto.getLastName() + "\n\n" +
                        "Link:" + link;
                email.setBody(body);
                mailMessage.setText(email.getBody());

                javaMailSender.send(mailMessage);

            } catch (Exception e) {

                throw new BadRequestException("Error while sending mail!");

            }

        }



        return employeeDto;

    }

    public String activateAccount(String username) {

        Optional<Employee> employee = employeeRepository.findByUserName(username);
        if(employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with username " + username + " not found!");
        }

//        String link = "http://localhost:4200/account-activated/" + username;
        String link = "http://localhost:8080/employees/activate-account/" + username;
        Optional<ActivationLink> activationLink = activationLinkRepository.findByLink(link);
        if(activationLink.isEmpty()) {
            throw new ResourceNotFoundException("Activation link invalid!");
        }
        activationLinkRepository.delete(activationLink.get());

        employee.get().setEnabled(true);
        employeeRepository.save(employee.get());
        EmployeeDto employeeDto = employeeMapper.mapToDto(employee.get());
        employeeDto.setRole(employee.get().getRoles().iterator().next().getName());

//        return employeeDto;
        return "Account " + username + "activated !";

    }

    public List<EmployeeDto> getAll() {

        List<Employee> employees = employeeRepository.findAllByOrderByEnabledDesc();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (Employee e : employees) {
            EmployeeDto employeeDto = employeeMapper.mapToDto(e);
            employeeDto.setRole(e.getRoles().iterator().next().getName());
            employeeDtos.add(employeeDto);
        }

        return employeeDtos;

    }

    public List<EmployeeDto> getAllEnabled() {

        List<Employee> employees = employeeRepository.findAllByEnabled(true);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (Employee e : employees) {
            EmployeeDto employeeDto = employeeMapper.mapToDto(e);
            employeeDto.setRole(e.getRoles().iterator().next().getName());
            employeeDtos.add(employeeDto);
        }

        return employeeDtos;

    }

    public EmployeeDto getById(Long id) {

        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }
        EmployeeDto employeeDto = employeeMapper.mapToDto(employee.get());
        employeeDto.setRole(employee.get().getRoles().iterator().next().getName());

        return employeeDto;

    }

    public EmployeeDto getByUsername(String userName) {

        Optional<Employee> employee = employeeRepository.findByUserName(userName);
        if(employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with username " + userName + " not found!");
        }
        EmployeeDto employeeDto = employeeMapper.mapToDto(employee.get());
        employeeDto.setRole(employee.get().getRoles().iterator().next().getName());

        return employeeDto;

    }

    @Transactional
    public EmployeeDto update(Long id, String firstName, String lastName, String position, Integer salary, String role, String password) {

        Optional<Employee> employeeFound = employeeRepository.findById(id);
        if(employeeFound.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }

        Employee employee = employeeFound.get();

        if(firstName != null) {
            employee.setFirstName(firstName);
        }
        if(lastName != null) {
            employee.setLastName(lastName);
        }
        if(position != null) {
            employee.setPosition(position);
        }
        if(salary != 0) {
            employee.setSalary(salary);
        }

        if(role != null) {
            System.out.println("role = " + role);
            Optional<Role> roleFound = roleRepository.findByName(role);
            if (roleFound.isEmpty()) {
                throw new ResourceNotFoundException("Role " + role + " not found!");
            }
            Set<Role> employeeRoles = new HashSet<>();
            employeeRoles.add(roleFound.get());
            employee.setRoles(employeeRoles);
        }

        if((password != null) && (!password.equals("null")) && (!password.isEmpty())) {
            employee.setPassword(getEncodedPassword(password));
        }

        employeeRepository.save(employee);

        EmployeeDto employeeDto = employeeMapper.mapToDto(employee);
        employeeDto.setRole(employee.getRoles().iterator().next().getName());

        return employeeDto;

    }

    @Transactional
    public EmployeeDto updateEnabled(Long id, Boolean enabled) {

        Optional<Employee> employeeFound = employeeRepository.findById(id);
        if(employeeFound.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }

        Employee employee = employeeFound.get();

        employee.setEnabled(enabled);

        employeeRepository.save(employee);

        EmployeeDto employeeDto = employeeMapper.mapToDto(employee);
        employeeDto.setRole(employee.getRoles().iterator().next().getName());

        return employeeDto;

    }

    @Transactional
    public String changePassword(Long id, String password) {

        Optional<Employee> employeeFound = employeeRepository.findById(id);
        if(employeeFound.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }

        Employee employee = employeeFound.get();

        if(password != null) {
            employee.setPassword(getEncodedPassword(password));
        }

        employeeRepository.save(employee);

        return "Password changed!";

    }

    @Transactional
    public String changeRole(Long id, String roleName) {

        Optional<Employee> employeeFound = employeeRepository.findById(id);
        if(employeeFound.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }

        Employee employee = employeeFound.get();

        if(roleName != null) {
            Optional<Role> roleFound = roleRepository.findByName(roleName);
            if (roleFound.isEmpty()) {
                throw new ResourceNotFoundException("Role " + roleName + " not found!");
            }
            Role role = roleFound.get();
            Set<Role> employeeRoles = new HashSet<>();
            employeeRoles.add(role);
            employee.setRoles(employeeRoles);
        }

        employeeRepository.save(employee);

        return "Employee role changed to " + roleName + "!";

    }

    @Transactional
    public String delete (Long id) {

        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found!");
        }
//        userRepository.deleteUserRolesByUserId(id);
        employeeRepository.deleteById(id);

        return "Employee with id " + id + " deleted!";

    }

}
