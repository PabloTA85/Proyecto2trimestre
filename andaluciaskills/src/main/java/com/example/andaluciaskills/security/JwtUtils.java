package com.example.andaluciaskills.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final Key secretKey;
    private final long jwtExpirationMs;

    // Constructor con valores desde application.properties
    public JwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes()); // Convertir clave secreta en Key
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // Generar un token JWT
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.joining(","))) // Incluye roles separados por coma
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256) // Firma con clave secreta
                .compact();
    }

    // Obtener el nombre de usuario del token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Obtener las autoridades del token
    public Collection<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roles = claims.get("role", String.class);
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Validar el token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Error al validar el token: " + e.getMessage());
            return false;
        }

        // Obtener los roles del token

    }
}
