//Crear el entrenador y cliente automaticamente

package com.gimnasio.config;

import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;
import com.gimnasio.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repo, UsuarioService service) {
        return args -> {
            if (repo.count() == 0) {
                // Creamos al Entrenador (ADMIN)
                service.crear(new Usuario("Carlos Entrenador", "admin", "admin@gimnasio.com", "11111111", "admin123", "ADMIN"));
                
                // Creamos a la Cliente (CLIENTE)
                service.crear(new Usuario("Maria Cliente", "maria", "maria@gimnasio.com", "22222222", "maria123", "CLIENTE"));
                
                System.out.println("✅ Usuarios de prueba creados exitosamente.");
            }
        };
    }
}
