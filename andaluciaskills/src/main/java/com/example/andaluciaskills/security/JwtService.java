package com.example.andaluciaskills.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // Método para generar el token
    public String generateToken(UserDetails userDetails) {
        return jwtUtils.generateToken(userDetails);
    }

    // Método para obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    // Método para validar el token
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    
}

