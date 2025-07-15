package com.example.echo.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exp}")
    private long exp;

    private final long iat;

    public JwtService() {
        this.iat = Instant.now().toEpochMilli();
    }

    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, subject);
    }

    public String createToken(
            Map<String, Object> claims,
            String subject
    ) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(new Date(iat))
                .expiration(new Date(getValidation()))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(
            String token
    ) {
        final Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(
            String token
    ) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public long getValidation() {
        return iat + exp;
    }
}