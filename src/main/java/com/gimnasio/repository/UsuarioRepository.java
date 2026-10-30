//Es el que se comunica con la base de datos

package com.gimnasio.repository;

import com.gimnasio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Para buscar al usuario cuando hace login
    Optional<Usuario> findByUsername(String username);

    // Para validar que el correo no se repita
    boolean existsByEmail(String email);

    // Para validar que la cédula no se repita
    boolean existsByCedula(String cedula);

    //Para buscar por correo
    java.util.Optional<Usuario> findByEmail(String email);

    java.util.Optional<Usuario> findByCedula(String cedula);
}