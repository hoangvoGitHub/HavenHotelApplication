package com.threeht.havenhotelapplication.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,// client request
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        LOG.info("This is request.getHeader outside if else check" + request.getHeader("Authorization"));
        final String jwt;
        final String userEmail;
        // if the header is null or does not start with 'Bearer ' -> do the next filter in filterChain
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            LOG.info("This is request.getHeader from if else check" + request.getHeader("Authorization"));
            filterChain.doFilter(request, response);
            return;
        }
        // Otherwise the jwt token will be the string after 'Bearer ' in the header
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // todo extract the userEmail from JWT token;\

        // Check if the userEmail is null or the toke is authenticated yet?
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load userDetails from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                //Create a new UsernamePasswordAuthenticationToken from this userDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                // Extend the details with request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Update the authentication with this authToke
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Continue next filter
            filterChain.doFilter(request,response);

        }
    }
}
