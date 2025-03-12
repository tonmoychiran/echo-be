package com.example.goppho.services;

import com.example.goppho.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {


    private final SecretKey secretKey;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public JWTService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserEntity user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateRefreshToken(UserEntity user) {
        return generateRefreshToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserEntity user) {
        return buildToken(extraClaims, user, accessExpiration);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserEntity user) {
        return buildToken(extraClaims, user, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserEntity user, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUserEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserEntity user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUserEmail())) && !isTokenExpired(token);
    }
}