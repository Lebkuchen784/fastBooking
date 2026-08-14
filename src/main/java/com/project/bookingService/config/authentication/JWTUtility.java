package com.project.bookingService.config.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTUtility {

    @Value("${jwt.secret}")
    private String spookySecretString;

    public SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(spookySecretString.getBytes());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getSignKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public String generateToken(String email) {
        // Claims, email
        return createToken(new HashMap<>(), email);
    }

    private String createToken(Map<String, Object> claims, String emailAddress) {
        return Jwts.builder()
                   .claims(claims)
                   .subject(emailAddress)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                   .signWith(getSignKey())
                   .compact();
    }

    public Boolean validateToken(String token, String emailAddress) {
        String extractedEmail = extractEmail(token);
        return Objects.equals(extractedEmail, emailAddress) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
