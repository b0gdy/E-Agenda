package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.dtos.RoleDto;
import com.dissertation.authentication.entities.Employee;
import com.dissertation.authentication.entities.Role;
import com.dissertation.authentication.exceptions.BadRequestException;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.mappers.RoleMapper;
import com.dissertation.authentication.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleDto create(Role role) {

        String name = role.getName();
        List<Role> roles = roleRepository.findAll();
        for (Role r : roles) {
            if(r.getName().equals(name)) {
                throw new BadRequestException("Role with name " + name + " already exists!");
            }
        }
        roleRepository.save(role);
        RoleDto roleDto = roleMapper.mapToDto(role);

        return roleDto;
        
    }

    public List<RoleDto> getAll() {
        
        List<Role> roles = roleRepository.findAll();
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role r : roles) {
            RoleDto roleDto = roleMapper.mapToDto(r);
            roleDtos.add(roleDto);
        }

        return roleDtos;
    
    }

    public RoleDto getById(Long id) {

        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) {
            throw new ResourceNotFoundException("Role with id " + id + " not found!");
        }
        RoleDto roleDto = roleMapper.mapToDto(role.get());

        return roleDto;

    }

    public RoleDto getByName(String name) {

        Optional<Role> role = roleRepository.findByName(name);
        if(role.isEmpty()) {
            throw new ResourceNotFoundException("Role " + name + " not found!");
        }
        RoleDto roleDto = roleMapper.mapToDto(role.get());

        return roleDto;

    }

}
