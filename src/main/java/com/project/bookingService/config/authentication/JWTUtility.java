package com.project.bookingService.config.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JWTUtility {

    public Key getSignKey() {
        String keySeed = "995c1865e8b0dbf557face906fd220fd45288bafecb66baa1397d00fe58f25244051193668b27e2984b88f2e7f1b9300d802df0f3231def3ee2486c61f6cec68";
        return Keys.hmacShaKeyFor(keySeed.getBytes());
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
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email) {
        // Claims, email
        return createToken(new HashMap<>(), email);
    }

    private String createToken(Map<String, Object> claims, String emailAddress) {
        return Jwts.builder()
                   .claims(claims)
                   .subject(emailAddress)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                   .signWith(getSignKey())
                   .compact();
    }

    public Boolean validateToken(String token, String emailAddress) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(emailAddress) && !isTokenExpired(token));
    }
}
