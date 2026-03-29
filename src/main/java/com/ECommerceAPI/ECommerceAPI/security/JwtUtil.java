package com.ECommerceAPI.ECommerceAPI.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Reads the secret key from application.properties
    @org.springframework.beans.factory.annotation.Value("${app.jwt.secret}")
    private String jwtSecret;

    // Reads the expiry time from application.properties (86400000 ms = 24 hours)
    @org.springframework.beans.factory.annotation.Value("${app.jwt.expiration}")
    private String jwtExpiration;

    // Converts our secret string into a real cryptographic key
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // CREATES a token — call this when login is successful
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)   //who this token belongs to
                .setIssuedAt(new Date())    //when it was created
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpiration)))    //when it expires
                .signWith(getSigningKey())  //sign it with our secret key
                .compact(); //build it into a string
    }

    // READS a token — extracts the username from inside it
    public  String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    // VALIDATES a token — checks its real, not expired, not tampered with
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;   // no exception = token is valid
        } catch (JwtException e) {
            return false;  // any exception = token is bad
        }
    }

}
