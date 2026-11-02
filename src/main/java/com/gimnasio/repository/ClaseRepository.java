package com.gimnasio.repository;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    
    // Para listar las clases ordenadas por fecha
    List<Clase> findAllByOrderByFechaInicioAsc();

    // Para validar traslapes de un entrenador
    // Busca clases del mismo entrenador donde el inicio sea menor al fin de la nueva clase y el fin sea mayor al inicio de la nueva clase.
    List<Clase> findByEntrenadorAndFechaInicioLessThanAndFechaFinGreaterThan(
            Usuario entrenador, LocalDateTime fechaFin, LocalDateTime fechaInicio);
}