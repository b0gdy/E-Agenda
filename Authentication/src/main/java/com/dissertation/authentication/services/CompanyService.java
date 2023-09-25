package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.CompanyDto;
import com.dissertation.authentication.entities.Company;
import com.dissertation.authentication.exceptions.BadRequestException;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.mappers.CompanyMapper;
import com.dissertation.authentication.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;
    
    public CompanyDto create(CompanyDto companyDto) {

        Company company = companyMapper.mapToEntity(companyDto);

        String name = companyDto.getName();
        List<Company> companies = companyRepository.findAll();
        for (Company c : companies) {
            if(c.getName().equals(name)) {
                throw new BadRequestException("Company with name " + name + " already exists!");
            }
        }

        companyRepository.save(company);
        companyDto = companyMapper.mapToDto(company);

        return companyDto;

    }

    public List<CompanyDto> getAll() {

        List<Company> companies = companyRepository.findAll();
        List<CompanyDto> companyDtos = new ArrayList<>();
        for (Company cc : companies) {
            CompanyDto companyDto = companyMapper.mapToDto(cc);
            companyDtos.add(companyDto);
        }

        return companyDtos;

    }

    public CompanyDto getById(Long id) {

        Optional<Company> company = companyRepository.findById(id);
        if(company.isEmpty()) {
            throw new ResourceNotFoundException("Company with id " + id + " not found!");
        }
        CompanyDto companyDto = companyMapper.mapToDto(company.get());

        return companyDto;

    }

    public CompanyDto getByName(String name) {

        Optional<Company> company = companyRepository.findByName(name);
        if(company.isEmpty()) {
            throw new ResourceNotFoundException("Company with name " + name + " not found!");
        }
        CompanyDto companyDto = companyMapper.mapToDto(company.get());

        return companyDto;

    }

    @Transactional
    public CompanyDto update(Long id, String name) {

        Optional<Company> companyFound = companyRepository.findById(id);
        if(companyFound.isEmpty()) {
            throw new ResourceNotFoundException("Company with id " + id + " not found!");
        }
        Company company = companyFound.get();

        if(name != null) {
            if(name.equals(company.getName())) {
                throw new BadRequestException("Company name is the same!");
            }
            List<Company> companies = companyRepository.findAll();
            for (Company c : companies) {
                if(c.getName().equals(name)) {
                    throw new BadRequestException("Company with name " + name + " already exists!");
                }
            }
            company.setName(name);
        }

        companyRepository.save(company);
        CompanyDto companyDto = companyMapper.mapToDto(company);

        return companyDto;

    }

    @Transactional
    public String delete (Long id) {

        Optional<Company> company = companyRepository.findById(id);
        if(company.isEmpty()) {
            throw new ResourceNotFoundException("Company with id " + id + " not found!");
        }
        companyRepository.deleteById(id);

        return "Company with id " + id + " deleted!";

    }
    
}
