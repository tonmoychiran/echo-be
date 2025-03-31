package com.example.goppho.configs;


import com.example.goppho.entities.UserEntity;
import com.example.goppho.services.GopphoUserDetailService;
import com.example.goppho.services.JwtService;
import com.example.goppho.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "/api/v1/auth/login/verification",
            "/api/v1/auth/login/verification/otp",
            "/api/v1/auth/login/verification/otp/resend"
    );
    private JwtService jwtService;
    ApplicationContext context;

    @Autowired
    public JwtFilter(
            JwtService jwtService,
            ApplicationContext context
    ) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (ALLOWED_PATHS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(request.getHeaderNames());

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String subject = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            subject = jwtService.extractSubject(token);
        }

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(GopphoUserDetailService.class).loadUserByUsername(subject);
            if (jwtService.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}