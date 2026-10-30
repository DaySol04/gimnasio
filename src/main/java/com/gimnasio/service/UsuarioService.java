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

    // Dependencias por el constructor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos los usuarios (panel de admin)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    // Buscar por Username (para el Login)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    // CREAR USUARIO (validaciones del Módulo 1 y 2)
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

    // DESACTIVAR (Borrado lógico)
    public void desactivar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    //Genera una contraseña temporal
    public String recuperarPassword(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("No existe una cuenta con ese correo"));
    
    // Genera una contraseña temporal aleatoria (ej: "temp1234")
    String passTemporal = "temp" + (int)(Math.random() * 9000 + 1000);
    
    // Se encripta y se guarda
    usuario.setPassword(passwordEncoder.encode(passTemporal));
    usuarioRepository.save(usuario);
    
    return passTemporal; // Se retorna la contraseña en texto plano para mostrarla en pantalla
}


    public void actualizar(Usuario usuarioEditado) {
        // 1. Se busca el usuario original en la base de datos
        Usuario usuarioDB = buscarPorId(usuarioEditado.getId());

        // 2. Validar correo único (que no sea el mismo de este usuario)
        usuarioRepository.findByEmail(usuarioEditado.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(usuarioEditado.getId())) {
                throw new IllegalArgumentException("El correo ya está registrado por otro usuario");
            }
        });

        // 3. Validar cédula única
        usuarioRepository.findByCedula(usuarioEditado.getCedula()).ifPresent(u -> {
            if (!u.getId().equals(usuarioEditado.getId())) {
                throw new IllegalArgumentException("La cédula ya está registrada por otro usuario");
            }
        });

        // 4. Validar username único
        usuarioRepository.findByUsername(usuarioEditado.getUsername()).ifPresent(u -> {
            if (!u.getId().equals(usuarioEditado.getId())) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso");
            }
        });

        // 5. Manejo de la contraseña: si viene vacía, se mantiene la que ya tenía
        if (usuarioEditado.getPassword() == null || usuarioEditado.getPassword().trim().isEmpty()) {
            usuarioEditado.setPassword(usuarioDB.getPassword());
        } else {
            // Si escribió una nueva, se encripta
            usuarioEditado.setPassword(passwordEncoder.encode(usuarioEditado.getPassword()));
        }

        // 6. Se mantiene el rol y el estado original 
        usuarioEditado.setRol(usuarioDB.getRol());
        usuarioEditado.setActivo(usuarioDB.isActivo());

        usuarioRepository.save(usuarioEditado);
    }
}