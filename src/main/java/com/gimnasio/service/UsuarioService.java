// Aquí va la lógica, donde se encriptará la contraseña y se validará que no se dupliquen correos y cédulas

package com.gimnasio.service;

import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyectamos las dependencias por el constructor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos los usuarios (para el panel de admin)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    // Buscar por Username (lo usaremos para el Login)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    // CREAR USUARIO (Aquí van las validaciones del Módulo 1 y 2)
    public void crear(Usuario usuario) {
        // Validar correo único
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        // Validar cédula única
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }
        
        // Encriptar la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Guardar en la base de datos
        usuarioRepository.save(usuario);
    }

    // DESACTIVAR (Borrado lógico, para el Módulo 2)
    public void desactivar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}