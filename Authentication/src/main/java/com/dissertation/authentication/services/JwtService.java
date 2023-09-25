package com.dissertation.authentication.services;

import com.dissertation.authentication.dtos.JwtRequestDto;
import com.dissertation.authentication.dtos.JwtResponseDto;
import com.dissertation.authentication.dtos.RoleTokenDto;
import com.dissertation.authentication.dtos.UserTokenDto;
import com.dissertation.authentication.entities.Role;
import com.dissertation.authentication.entities.User;
import com.dissertation.authentication.exceptions.ResourceNotFoundException;
import com.dissertation.authentication.repositories.UserRepository;
import com.dissertation.authentication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponseDto createJwtToken(JwtRequestDto jwtRequestDto) {
        String userName = jwtRequestDto.getUserName();
        String password = jwtRequestDto.getPassword();

        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("Username not found!");
        }

        if(!user.get().isEnabled()) {
            throw new DisabledException("User is disabled!");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled!");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials from user!");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getPassword(), getAuthorities(user.get()));

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        Long id = user.get().getId();
        Role role = user.get().getRoles().iterator().next();

        return new JwtResponseDto(id, userName, role.getName(), newGeneratedToken);

    }

    public String disableUser(Long id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("Username not found!");
        }

        user.get().setEnabled(false);
        userRepository.save(user.get());

        return "User disabled!";

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Username is not valid");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getPassword(), getAuthorities(user.get()));
    }

    private Set getAuthorities(User user) {
        Set authorities = new HashSet();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        return authorities;
    }

    private void authenticate(String userName, String password) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials from user");
        }

    }

    public UserTokenDto getUserByToken(String token) {

        String jwtToken = token;

        String userName = jwtUtil.getUserNameFromToken(jwtToken);
        Optional<User> userFound = userRepository.findByUserName(userName);
        if(!userFound.isEmpty()) {
            User user = userFound.get();
            UserTokenDto userTokenDto = new UserTokenDto();
            userTokenDto.setId(user.getId());
            userTokenDto.setUserName(user.getUserName());
            userTokenDto.setPassword(user.getPassword());
            Set<RoleTokenDto> roleTokenDtos = new HashSet<>();
            user.getRoles().forEach(role -> {
                RoleTokenDto roleTokenDto = new RoleTokenDto();
                roleTokenDto.setId(role.getId());
                roleTokenDto.setName(role.getName());
                roleTokenDtos.add(roleTokenDto);
            });
            userTokenDto.setRoles(roleTokenDtos);
            return userTokenDto;
        } else {
            throw new UsernameNotFoundException("Username is not valid!");
        }

    }

    public UserTokenDto getUserByUserName(String userName) {

        Optional<User> userFound = userRepository.findByUserName(userName);
        if(!userFound.isEmpty()) {
            User user = userFound.get();
            UserTokenDto userTokenDto = new UserTokenDto();
            userTokenDto.setId(user.getId());
            userTokenDto.setUserName(user.getUserName());
            userTokenDto.setPassword(user.getPassword());
            Set<RoleTokenDto> roleTokenDtos = new HashSet<>();
            user.getRoles().forEach(role -> {
                RoleTokenDto roleTokenDto = new RoleTokenDto();
                roleTokenDto.setId(role.getId());
                roleTokenDto.setName(role.getName());
                roleTokenDtos.add(roleTokenDto);
            });
            userTokenDto.setRoles(roleTokenDtos);
            return userTokenDto;
        } else {
            throw new UsernameNotFoundException("Username is not valid!");
        }

    }

}
