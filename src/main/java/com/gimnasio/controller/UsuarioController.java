package com.gimnasio.controller;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Mostrar la lista de todos los usuarios (Solo para ADMIN)
    @GetMapping
    public String listarUsuarios(Model model, Authentication authentication) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usernameActual", authentication.getName()); // Para saber quién está logueado
        return "usuarios/lista"; // Buscará en templates/usuarios/lista.html
    }
    
        // Desactivar un usuario (Borrado lógico)
    @PostMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable("id") Long id) {
        usuarioService.desactivar(id);
        return "redirect:/usuarios"; // Recarga la tabla para ver el cambio
    }
}
