package com.gimnasio.service;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.ClaseRepository;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final UsuarioRepository usuarioRepository;

    public ClaseService(ClaseRepository claseRepository, UsuarioRepository usuarioRepository) {
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Para obtener las clases ordenadas por fecha
    public List<Clase> listarTodas() {
        return claseRepository.findAllByOrderByFechaInicioAsc();
    }

    public void guardar(Clase clase) {
        // Calcula la hora de fin (inicio + 1 hora)
        LocalDateTime inicio = clase.getFechaInicio();
        LocalDateTime fin = inicio.plusHours(1);
        clase.setFechaFin(fin);

        // Valida si hay choque de horario con el entrenador, se busca si el entrenador ya tiene una clase que se cruce con este horario
        List<Clase> choques = claseRepository.findByEntrenadorAndFechaInicioLessThanAndFechaFinGreaterThan(
                clase.getEntrenador(), fin, inicio);

        if (!choques.isEmpty()) {
            throw new IllegalArgumentException("El entrenador ya tiene una clase asignada en este horario.");
        }

        // Guarda en la base de datos
        claseRepository.save(clase);
    }
}