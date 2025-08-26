package com.LostFound.MainProject.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.LostFound.MainProject.Entities.Roles;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String secret ="zYxHx8bq3FLlG4SlBPXTHCTAchWuyyUlzKXVXubJIl2dlTpCp/YlQIqyYhtaISO9evzxSGM6nE7jQiPxIVpIqw==";
	private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    private final long expirationMs = 86400000; // 24 hours

    public String generateToken(String username, Roles role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public Roles extractRoleEnum(String token) {
        String roleStr = extractRole(token);
        return Roles.fromString(roleStr);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
