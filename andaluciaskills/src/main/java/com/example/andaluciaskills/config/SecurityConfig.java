package com.example.andaluciaskills.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.andaluciaskills.security.JwtAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@Lazy
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http)) 
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/usuario/**",
                                "/api/participante/**", "/uploads/**", "/api/especialidad/**", "/api/evaluacion-items/**","/api/pruebas/**", "/api/participante/porEspecialidad/**", "/api/evaluaciones/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html")
                        
                        .permitAll()
                        //GET
                        .requestMatchers("/api/pruebas/**","/api/items/**").hasRole("EXPERTO")
                        

                         //POST                   
                        .requestMatchers(HttpMethod.POST, "/api/admin/**", "/api/especialidad/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/experto/**", "/api/participante/create"
                        )
                        .hasRole("EXPERTO")
                        //PUT
                        .requestMatchers(HttpMethod.PUT, "/api/participante/{id}").hasRole("ADMIN")  

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
