package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.ClientDto;
import com.dissertation.authentication.dtos.ClientRegisterDto;
import com.dissertation.authentication.dtos.CompanyDto;
import com.dissertation.authentication.dtos.EmployeeDto;
import com.dissertation.authentication.entities.*;
import com.dissertation.authentication.exceptions.BadRequestException;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.mappers.ClientMapper;
import com.dissertation.authentication.mappers.ClientRegisterMapper;
import com.dissertation.authentication.mappers.CompanyMapper;
import com.dissertation.authentication.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientRegisterMapper clientRegisterMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private ActivationLinkRepository activationLinkRepository;
    
    public String getEncodedPassword(String password) {

        return passwordEncoder.encode(password);

    }

    public ClientDto create(ClientRegisterDto clientRegisterDto) {

        Client client = clientRegisterMapper.mapToEntity(clientRegisterDto);

        String userName = clientRegisterDto.getUserName();
        List<Client> clients = clientRepository.findAll();
        for (Client c : clients) {
            if(c.getUserName().equals(userName)) {
                throw new BadRequestException("Client with username " + userName + " already exists!");
            }
        }

        String password = clientRegisterDto.getPassword();
        client.setPassword(getEncodedPassword(password));

//        client.setEnabled(true);
        client.setEnabled(false);

//        String roleName = clientRegisterDto.getRole();
        String roleName = "client";
        Role role = roleRepository.findByName(roleName).get();
        Set<Role> clientRoles = new HashSet<>();
        clientRoles.add(role);
        client.setRoles(clientRoles);

        Optional<Company> companyFound = companyRepository.findById(clientRegisterDto.getCompanyId());
        if (companyFound.isEmpty()) {
            throw new ResourceNotFoundException("Company with id " + clientRegisterDto.getCompanyId() + " not found!");
        }
        Company company = companyFound.get();
        client.setCompany(company);
        CompanyDto companyDto = companyMapper.mapToDto(company);

        clientRepository.save(client);
        ClientDto clientDto = clientMapper.mapToDto(client);
        clientDto.setRole(client.getRoles().iterator().next().getName());
        clientDto.setCompanyDto(companyDto);



        if(clientDto.getUserName().equals("client7845@gmail.com")) {

            try {

                Email email = new Email();

                SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setFrom(sender);

                email.setRecipient(clientDto.getUserName());
                mailMessage.setTo(email.getRecipient());

                email.setSubject("Activate your account!");
                mailMessage.setSubject(email.getSubject());

//                String link = "http://localhost:4200/account-activated/" + clientDto.getUserName();
                String link = "http://localhost:8080/clients/activate-account/" + clientDto.getUserName();
                ActivationLink activationLink = new ActivationLink();
                activationLink.setLink(link);
                activationLinkRepository.save(activationLink);

                String body = "";
                body = body + "Hey!" + "\n" + "Please click the following link to activate your account!\n\n" +
                        "Username: " + clientDto.getUserName() + "\n" +
                        "Firstname: " + clientDto.getFirstName() + "\n" +
                        "Lastname: " + clientDto.getLastName() + "\n\n" +
                        "Link:" + link;
                email.setBody(body);
                mailMessage.setText(email.getBody());

                javaMailSender.send(mailMessage);

            } catch (Exception e) {

                throw new BadRequestException("Error while sending mail!");

            }

        }
        
        
        
        
        return clientDto;

    }

    public String activateAccount(String username) {

        Optional<Client> client = clientRepository.findByUserName(username);
        if(client.isEmpty()) {
            throw new ResourceNotFoundException("Client with username " + username + " not found!");
        }

//        String link = "http://localhost:4200/account-activated/" + username;
        String link = "http://localhost:8080/clients/activate-account/" + username;
        Optional<ActivationLink> activationLink = activationLinkRepository.findByLink(link);
        if(activationLink.isEmpty()) {
            throw new ResourceNotFoundException("Activation link invalid!");
        }
        activationLinkRepository.delete(activationLink.get());

        client.get().setEnabled(true);
        clientRepository.save(client.get());
        ClientDto clientDto = clientMapper.mapToDto(client.get());
        clientDto.setRole(client.get().getRoles().iterator().next().getName());

//        return clientDto;
        return "Account " + username + "activated !";

    }

    public List<ClientDto> getAll() {

        List<Client> clients = clientRepository.findAllByOrderByEnabledDesc();
        List<ClientDto> clientDtos = new ArrayList<>();
        for (Client c : clients) {
            ClientDto clientDto = clientMapper.mapToDto(c);
            clientDto.setRole(c.getRoles().iterator().next().getName());
            clientDto.setCompanyDto(companyMapper.mapToDto(c.getCompany()));
            clientDtos.add(clientDto);
        }

        return clientDtos;

    }

    public List<ClientDto> getAllEnabled() {

        List<Client> clients = clientRepository.findAllByEnabled(true);
        List<ClientDto> clientDtos = new ArrayList<>();
        for (Client c : clients) {
            ClientDto clientDto = clientMapper.mapToDto(c);
            clientDto.setRole(c.getRoles().iterator().next().getName());
            clientDto.setCompanyDto(companyMapper.mapToDto(c.getCompany()));
            clientDtos.add(clientDto);
        }

        return clientDtos;

    }

    public List<ClientDto> getByCompany(Long companyId) {

        Optional<Company> companyFound = companyRepository.findById(companyId);
        if (companyFound.isEmpty()) {
            throw new ResourceNotFoundException("Company with id " + companyId + " not found!");
        }
        Company company = companyFound.get();

        List<Client> clients = clientRepository.findByCompanyId(companyId);
        List<ClientDto> clientDtos = new ArrayList<>();
        for (Client c : clients) {
            ClientDto clientDto = clientMapper.mapToDto(c);
            clientDto.setRole(c.getRoles().iterator().next().getName());
            clientDto.setCompanyDto(companyMapper.mapToDto(c.getCompany()));
            clientDtos.add(clientDto);
        }

        return clientDtos;

    }

    public ClientDto getById(Long id) {

        Optional<Client> clientFound = clientRepository.findById(id);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }
        Client client = clientFound.get();
        ClientDto clientDto = clientMapper.mapToDto(client);
        clientDto.setRole(client.getRoles().iterator().next().getName());
        clientDto.setCompanyDto(companyMapper.mapToDto(client.getCompany()));

        return clientDto;

    }

    public ClientDto getByUsername(String userName) {

        Optional<Client> clientFound = clientRepository.findByUserName(userName);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with username " + userName + " not found!");
        }
        Client client = clientFound.get();
        ClientDto clientDto = clientMapper.mapToDto(client);
        clientDto.setRole(client.getRoles().iterator().next().getName());
        clientDto.setCompanyDto(companyMapper.mapToDto(client.getCompany()));

        return clientDto;

    }

    @Transactional
    public ClientDto update(Long id, String firstName, String lastName, Long companyId, String password) {

        Optional<Client> clientFound = clientRepository.findById(id);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }

        Client client = clientFound.get();

        if(firstName != null) {
            client.setFirstName(firstName);
        }
        if(lastName != null) {
            client.setLastName(lastName);
        }

        if(companyId != null) {
            Optional<Company> companyFound = companyRepository.findById(companyId);
            if (companyFound.isEmpty()) {
                throw new ResourceNotFoundException("Company with id " + companyId + " not found!");
            }
            Company company = companyFound.get();
            client.setCompanyId(company.getId());
            client.setCompany(company);
        }

        if((password != null) && (!password.equals("null")) && (!password.isEmpty())) {
            client.setPassword(getEncodedPassword(password));
        }

        clientRepository.save(client);

        ClientDto clientDto = clientMapper.mapToDto(client);
        clientDto.setRole(client.getRoles().iterator().next().getName());
        clientDto.setCompanyDto(companyMapper.mapToDto(client.getCompany()));

        return clientDto;

    }

    @Transactional
    public ClientDto updateEnabled(Long id, Boolean enabled) {

        Optional<Client> clientFound = clientRepository.findById(id);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }

        Client client = clientFound.get();

        client.setEnabled(enabled);

        clientRepository.save(client);

        ClientDto clientDto = clientMapper.mapToDto(client);
        clientDto.setRole(client.getRoles().iterator().next().getName());
        clientDto.setCompanyDto(companyMapper.mapToDto(client.getCompany()));

        return clientDto;

    }

    @Transactional
    public String changePassword(Long id, String password) {

        Optional<Client> clientFound = clientRepository.findById(id);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }

        Client client = clientFound.get();

        if(password != null) {
            client.setPassword(getEncodedPassword(password));
        }

        clientRepository.save(client);

        return "Password changed!";

    }

    @Transactional
    public String changeRole(Long id, String roleName) {

        Optional<Client> clientFound = clientRepository.findById(id);
        if(clientFound.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }

        Client client = clientFound.get();

        if(roleName != null) {
            Optional<Role> roleFound = roleRepository.findByName(roleName);
            if (roleFound.isEmpty()) {
                throw new ResourceNotFoundException("Role " + roleName + " not found!");
            }
            Role role = roleFound.get();
            Set<Role> clientRoles = new HashSet<>();
            clientRoles.add(role);
            client.setRoles(clientRoles);
        }

        clientRepository.save(client);

        return "Client role changed to " + roleName + "!";

    }

    @Transactional
    public String delete (Long id) {

        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty()) {
            throw new ResourceNotFoundException("Client with id " + id + " not found!");
        }
//        userRepository.deleteUserRolesByUserId(id);
        clientRepository.deleteById(id);

        return "Client with id " + id + " deleted!";

    }

}
