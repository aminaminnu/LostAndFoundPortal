package com.LostFound.MainProject.Security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.LostFound.MainProject.Entities.Roles;
import com.LostFound.MainProject.Entities.User;
import com.LostFound.MainProject.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        String role = null;

        // Extract token and user info
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(token);
                role = jwtUtil.extractRole(token);
            } catch (Exception e) {
                logger.error("JWT parsing failed", e);
            }
        }

        // Authenticate user if token is valid and not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null && jwtUtil.validateToken(token)) {
                try {
                    // Ensure role is prefixed properly
                    Roles roleEnum = Roles.fromString(role);
                    String roleWithPrefix = role != null && role.startsWith("ROLE_")
                            ? role
                            : "ROLE_" + roleEnum.name();

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix))
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } catch (IllegalArgumentException e) {
                    logger.error("Invalid role from token: " + role);
                }
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
