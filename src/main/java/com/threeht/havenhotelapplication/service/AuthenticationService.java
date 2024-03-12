package com.threeht.havenhotelapplication.service;

import com.threeht.havenhotelapplication.request.RegisterRequest;
import com.threeht.havenhotelapplication.exception.UserAlreadyExistsException;
import com.threeht.havenhotelapplication.repository.UserRepository;
import com.threeht.havenhotelapplication.model.Role;
import com.threeht.havenhotelapplication.model.User;
import com.threeht.havenhotelapplication.request.AuthenticationRequest;
import com.threeht.havenhotelapplication.response.JwtResponse;
import com.threeht.havenhotelapplication.security.jwt.JwtUtils;
import com.threeht.havenhotelapplication.security.user.HotelUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtils jwtUtils;
        private final AuthenticationManager authenticationManager;

        public User register(RegisterRequest request) {
                String email = request.getEmail();
                if (userRepository.existsByEmail(email)) {
                        throw new UserAlreadyExistsException(email + " already exists");
                }

                var user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .role(request.getRole())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .build();

                return userRepository.save(user);
        }

        public JwtResponse authenticate(AuthenticationRequest request) {
                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwtToken = jwtUtils.generateJwtTokenForUser(authentication);
                HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority).toList();
                return new JwtResponse(
                                userDetails.getId(),
                                userDetails.getEmail(),
                                jwtToken,
                                roles);

        }

}
