package com.blogapp.blogsphere.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")      //Spring annotation used to read values from configuration files(application.properties)
    private String secret;    //secret = blogsphere_super_secret_key_2024_make_it_long

    @Value("${jwt.expiration}")
    private Long expiration;     //expiration = 86400000;

    // Generate token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    // Get email from token
    public String extractEmail(String token) {  //Opens the token and reads email stored inside
        return getClaims(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String email) {     //Checks 2 things: 1.Email in token matches database emai  2.Token is not expired
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    // Check if token expired
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Get claims from token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Get secret key
    private SecretKey getSecretKey() {        //Converts your secret string into a proper security key
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}