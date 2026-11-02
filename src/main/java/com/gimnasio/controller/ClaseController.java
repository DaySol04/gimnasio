package com.gimnasio.controller;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClaseService;
import com.gimnasio.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clases")
public class ClaseController {

    private final ClaseService claseService;
    private final UsuarioService usuarioService;

    public ClaseController(ClaseService claseService, UsuarioService usuarioService) {
        this.claseService = claseService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clases", claseService.listarTodas());
        return "clases/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clase", new Clase());
        // Se obtienen solo los usuarios que son ADMIN para que aparezcan en el select de entrenadores
        List<Usuario> entrenadores = usuarioService.listarTodos().stream()
                .filter(u -> u.getRol().equals("ADMIN"))
                .toList();
        model.addAttribute("entrenadores", entrenadores);
        return "clases/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("clase") Clase clase, BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            
            List<Usuario> entrenadores = usuarioService.listarTodos().stream()
                    .filter(u -> u.getRol().equals("ADMIN"))
                    .toList();
            model.addAttribute("entrenadores", entrenadores);
            return "clases/formulario";
        }
        try {
            claseService.guardar(clase);
            flash.addFlashAttribute("mensaje", "Clase registrada exitosamente");
            return "redirect:/clases";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            List<Usuario> entrenadores = usuarioService.listarTodos().stream()
                    .filter(u -> u.getRol().equals("ADMIN"))
                    .toList();
            model.addAttribute("entrenadores", entrenadores);
            return "clases/formulario";
        }
    }
}