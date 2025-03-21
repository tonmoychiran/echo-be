package com.example.goppho.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
        return createToken(claims, subject, exp);
    }

    public String createToken(
            Map<String, Object> claims,
            String subject,
            long exp
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
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
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

    public boolean validateToken(
            String token,
            String id
    ) {
        final String subject = extractSubject(token);
        return (subject.equals(id) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(
            String token
    ) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(
            String token
    ) {
        return extractClaim(token, Claims::getExpiration);
    }

    public long getValidation() {
        return iat + exp;
    }
}