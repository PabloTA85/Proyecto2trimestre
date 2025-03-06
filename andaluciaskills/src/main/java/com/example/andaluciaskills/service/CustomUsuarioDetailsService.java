package com.example.andaluciaskills.service;

import com.example.andaluciaskills.model.Usuario;
import com.example.andaluciaskills.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public CustomUsuarioDetailsService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para encontrar un usuario por su nombre de usuario
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRole()) 
                .build();
    }

    private GrantedAuthority getAuthoritiesForUser(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Obtener roles del usuario y convertirlos en objetos GrantedAuthority
        return new SimpleGrantedAuthority("ROLE_" + usuario.getRole().toUpperCase());
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    public Usuario registerUser(Usuario user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptamos la contraseña
        return usuarioRepository.save(user);
    }

    public static String authenticateUser(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateUser'");
    }
}
