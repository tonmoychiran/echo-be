package com.example.echo.configs;

import com.example.echo.entities.UserAuthOTPEntity;
import com.example.echo.exceptions.AuthorizationHeaderMissingException;
import com.example.echo.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/verification");
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        try {
            String token = getToken(request);

            String userEmail = jwtService.extractSubject(token);
            UserAuthOTPEntity userDetails = (UserAuthOTPEntity) userDetailsService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }

    private String getToken(
            HttpServletRequest request
    ) {
        if (request.getRequestURI().equals("/chat")) {
            String token = request.getParameter("token");
            if (token == null || token.isBlank()) {
                throw new AuthorizationHeaderMissingException("Token query parameter not found");
            }
            return token;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new AuthorizationHeaderMissingException("Authorization header not found");
        }

        if (!authHeader.startsWith("Bearer ")) {
            throw new InvalidBearerTokenException("Bearer token not found");
        }

        return authHeader.substring(7);
    }
}