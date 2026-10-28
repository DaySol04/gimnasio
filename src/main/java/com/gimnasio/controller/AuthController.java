// Para mostrar el formulario y procesar los datos

package com.gimnasio.controller;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller

public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // (Deja aquí el @GetMapping("/login") y @GetMapping("/") que ya tenías)

    // 1. Mostrar el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Buscará registro.html
    }

    // 2. Procesar el formulario
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        // Si hay errores de formato (ej. correo mal escrito, cédula vacía)
        if (result.hasErrors()) {
            return "registro"; // Vuelve al formulario con los errores
        }

        try {
            // Forzamos que el rol siempre sea CLIENTE al registrarse por la web
            usuario.setRol("CLIENTE");
            usuarioService.crear(usuario); // Aquí salta el error de correo/cédula duplicado
            
            // Si todo sale bien, lo mandamos al login con un mensaje
            return "redirect:/login?exito";
        } catch (IllegalArgumentException e) {
            // Capturamos el error de "El correo ya está registrado" o "Cédula ya registrada"
            model.addAttribute("errorRegistro", e.getMessage());
            return "registro";
        }
    }
}
