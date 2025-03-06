package com.example.andaluciaskills.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        // Si la ruta es pública, no se necesita autenticación
        if (requestURI.equals("/api/participante/all")
                || requestURI.equals("/api/auth/login")
                || requestURI.equals("/api/auth/register")
                || requestURI.startsWith("/api/usuarios/")) {
            // Log para depurar si el filtro está saltando estas rutas
            logger.info("Ruta pública detectada: " + requestURI);
            filterChain.doFilter(request, response); // Continuar con la cadena de filtros sin autenticar
            return;
        }

        // Si la ruta no es pública, se debe autenticar
        String token = getTokenFromRequest(request);

        if (token != null) {
            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Log if the token is invalid
                logger.warn("Invalid JWT token: " + token);
            }
        } else {
            // Log if no token is found in the request
            logger.warn("No JWT token found in request");
        }

        filterChain.doFilter(request, response); // Continuar con el siguiente filtro
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extraer el token
        }
        return null;
    }
}
