//Se le dice a Spring que use la encriptacion, proteja las rutas y use el login personalizado

package com.gimnasio.config;

import com.gimnasio.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Conecta el servicio de usuarios con el encriptador de contraseñas
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    // Aqupi se definen las relas de accion, quien puede entrar a que ruta
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/login", "/registro", "/recuperar", "/css/**", "/js/**").permitAll()
                
                // Rutas de gestión de usuarios (Solo ADMIN)
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                
                // Rutas de creación de clases (Solo ADMIN)
                .requestMatchers("/clases/nuevo", "/clases/guardar").hasRole("ADMIN")
                
                // Cualquier otro usuario autenticado puede ver la lista de clases y su perfil
                .requestMatchers("/clases/**", "/perfil").authenticated()
                
                // Todo lo demás requiere estar logueado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}