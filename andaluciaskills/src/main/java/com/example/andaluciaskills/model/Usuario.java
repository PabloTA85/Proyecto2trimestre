package com.example.andaluciaskills.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;



@Entity
@Getter
@Setter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max = 20, message = "El nombre de usuario debe tener entre 5 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener el formato correcto (8 dígitos seguidos de una letra)")
    private String dni;

    @NotBlank(message = "El rol no puede estar vacío")
    private String role; // Puede ser ADMIN o EXPERTO

    @ManyToOne
    @JoinColumn(name = "Especialidad_id")
    @JsonBackReference
    private Especialidad especialidad;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Usamos un HashSet para garantizar la colección correcta
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.toUpperCase()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
