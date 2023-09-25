package com.dissertation.Meetings.configuration;

import com.dissertation.Meetings.dtos.UserTokenDto;
import com.dissertation.Meetings.services.UserService;
import com.dissertation.Meetings.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private UserService userService;

//    @Autowired
//    private RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        String jwtToken = null;
        String userName = null;

        if (header != null && header.startsWith("Bearer ")) {

            jwtToken = header.substring(7);

            try {
                userName = jwtUtil.getUserNameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                System.out.println("Jwt token is expired!");
            }

        } else {
            System.out.println("Jwt token does not start with Bearer");
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            System.out.println("Meetings SecurityContextHolder.getContext().getAuthentication() 1 = " + SecurityContextHolder.getContext().getAuthentication());

//            UserTokenDto userTokenDto = userService.getByToken(jwtToken, header);

            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/getuserbytoken?token=" + jwtToken)
                    .baseUrl("authentication:8080/getuserbytoken?token=" + jwtToken)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .build();
            Mono<UserTokenDto> userTokenDtoMono = webClient.get().retrieve().bodyToMono(UserTokenDto.class);
            UserTokenDto userTokenDto = userTokenDtoMono.block();

//            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/getuserbyusername?userName=" + userName)
////                    .baseUrl("authentication:8080/getbyusername?username=" + userName)
//                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
//                    .build();
//            Mono<UserTokenDto> userTokenDtoMono = webClient.get().retrieve().bodyToMono(UserTokenDto.class);
//            UserTokenDto userTokenDto = userTokenDtoMono.block();


            Set authorities = new HashSet();
            userTokenDto.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            });
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(userTokenDto.getUserName(), userTokenDto.getPassword(), authorities);

            if(jwtUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

//                System.out.println("Meetings SecurityContextHolder.getContext().getAuthentication() 2 = " + SecurityContextHolder.getContext().getAuthentication());

            }

//            System.out.println("Meetings SecurityContextHolder.getContext().getAuthentication() 3 = " + SecurityContextHolder.getContext().getAuthentication());

        }

//        System.out.println("Meetings SecurityContextHolder.getContext().getAuthentication() 4 = " + SecurityContextHolder.getContext().getAuthentication());
//        SecurityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
//        SecurityContext.getAuthentication();

        filterChain.doFilter(request, response);

    }

}
