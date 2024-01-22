package com.threeht.havenhotelapplication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    static final Long EXPIRATION_TIME = 1000L * 60 * 60 * 24; //1 day

    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * An authentication provider is responsible for authenticating user credentials and providing an authenticated Authentication object.
     */
    private final AuthenticationProvider authenticationProvider;

    @Bean
    //Config the security filter chain
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        SecurityContextRepository repo = new MyCustomSecurityContextRepository();

        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable csrf, which will not prevent unauthorized requests
                .authorizeHttpRequests(auth -> auth //Configures the authorization rules for incoming HTTP requests
//                        .requestMatchers("/api/v1/auth/**").permitAll() //Request that match the matchers will be permitted without requiring authentication.
                        .anyRequest().permitAll()
//                                .authenticated() // Other requests that does not match should be authenticated
                )
//                .exceptionHandling()
                .sessionManagement(sessionConfigure -> sessionConfigure
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


}