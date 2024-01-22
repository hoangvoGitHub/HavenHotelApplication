package com.threeht.havenhotelapplication.service;



import com.threeht.havenhotelapplication.request.RegisterRequest;
import com.threeht.havenhotelapplication.config.JwtService;
import com.threeht.havenhotelapplication.repository.UserRepository;
import com.threeht.havenhotelapplication.model.Role;
import com.threeht.havenhotelapplication.model.User;
import com.threeht.havenhotelapplication.request.AuthenticationRequest;
import com.threeht.havenhotelapplication.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request) {
        String username = request.getUsername();
        if (repository.findByUsername(username).isPresent()) {
            return AuthenticationResponse.builder()
                    .status(HttpStatus.NOT_IMPLEMENTED.value())
                    .message("Choose other username!")
                    .username(null)
                    .token(null)
                    .build();
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .createdAt(new Date())
                .roles(List.of(Role.USER))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Your account is successfully registered!")
                .username(request.getUsername())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
            var user = repository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .username(request.getUsername())
                    .token(jwtToken)
                    .status(HttpStatus.OK.value())
                    .message("Successful authentication!")
                    .build();
        } catch (UsernameNotFoundException e) {
            return AuthenticationResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .build();
        }
    }

}
