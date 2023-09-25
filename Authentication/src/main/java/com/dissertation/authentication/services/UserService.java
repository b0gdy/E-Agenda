package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.ClientDto;
import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.UserDto;
import com.dissertation.authentication.entities.*;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.mappers.UserMapper;
import com.dissertation.authentication.repositories.RoleRepository;
import com.dissertation.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserMapper userMapper;

    public String getEncodedPassword(String password) {

        return passwordEncoder.encode(password);

    }

    public List<UserDto> getAll() {

        List<User> users = userRepository.findAllByOrderByEnabledDesc();
        List<UserDto> userDtos = new ArrayList<>();
        for (User e : users) {
            UserDto userDto = userMapper.mapToDto(e);
            userDto.setRole(e.getRoles().iterator().next().getName());
            userDtos.add(userDto);
        }

        return userDtos;

    }

    public List<UserDto> getAllEnabled() {

        List<User> users = userRepository.findAllByEnabled(true);
        List<UserDto> userDtos = new ArrayList<>();
        for (User e : users) {
            UserDto userDto = userMapper.mapToDto(e);
            userDto.setRole(e.getRoles().iterator().next().getName());
            userDtos.add(userDto);
        }

        return userDtos;

    }
    
    public UserDto getById(Long id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }
        UserDto userDto = userMapper.mapToDto(user.get());
        userDto.setRole(user.get().getRoles().iterator().next().getName());

        return userDto;

    }

    public UserDto getByUsername(String userName) {

        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User with username " + userName + " not found!");
        }
        UserDto userDto = userMapper.mapToDto(user.get());
        userDto.setRole(user.get().getRoles().iterator().next().getName());

        return userDto;

    }

    @Transactional
    public UserDto update(Long id, String firstName, String lastName, String password) {

        Optional<User> userFound = userRepository.findById(id);
        if(userFound.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }

        User user = userFound.get();

        if(firstName != null) {
            user.setFirstName(firstName);
        }
        if(lastName != null) {
            user.setLastName(lastName);
        }

        if((password != null) && (!password.equals("null")) && (!password.isEmpty())) {
            user.setPassword(getEncodedPassword(password));
        }

        userRepository.save(user);

        UserDto userDto = userMapper.mapToDto(user);
        userDto.setRole(user.getRoles().iterator().next().getName());

        return userDto;

    }
    
    public User register(User user) {
        return userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).get();
    }

    public void initRolesAndUsers() {

        Role adminRole = new Role();
        Optional<Role> roleAdmin = roleRepository.findByName("admin");
        if(roleAdmin.isEmpty()) {

            adminRole.setName("admin");
            roleRepository.save(adminRole);
            System.out.println("Role admin created!");

        }

        Role employeeRole = new Role();
        Optional<Role> roleEmployee = roleRepository.findByName("employee");
        if(roleEmployee.isEmpty()) {

            employeeRole.setName("employee");
            roleRepository.save(employeeRole);
            System.out.println("Role employee created!");

        }

        Role clientRole = new Role();
        Optional<Role> roleClient = roleRepository.findByName("client");
        if(roleClient.isEmpty()) {

            clientRole.setName("client");
            roleRepository.save(clientRole);
            System.out.println("Role client created!");

        }

        User adminUser = new User();
        Optional<User> user = userRepository.findByUserName("admin");
        if(user.isEmpty()) {

            adminUser.setUserName("admin");
            adminUser.setPassword(getEncodedPassword("password"));
            adminUser.setEnabled(true);
            Set<Role> adminRoles = new HashSet<>();
            if(roleAdmin.isEmpty()) {
                adminRoles.add(adminRole);
            } else {
                adminRoles.add(roleAdmin.get());
            }
            adminUser.setRoles(adminRoles);
            userRepository.save(adminUser);
            System.out.println("User admin created!");

        }

    }

//    public String getEncodedPassword(String password) {
//        return passwordEncoder.encode(password);
//    }

}
