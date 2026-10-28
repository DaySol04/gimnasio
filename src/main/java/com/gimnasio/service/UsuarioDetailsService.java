// Conexion entre BD y sprin security, le dice al sistema como buscarl al usuario y que rol tiene

package com.gimnasio.service;

import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service 

public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Spring Security necesita saber el rol. En la BD guardamos "ADMIN" o "CLIENTE"
        // pero Spring Security espera que empiece con "ROLE_"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());

        // Retornamos el usuario adaptado a Spring Security
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isActivo(), // Si es false, no lo deja entrar
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.singletonList(authority)
        );
    }
}
