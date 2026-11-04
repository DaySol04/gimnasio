// Para mostrar el formulario y procesar los datos

package com.gimnasio.controller;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.UsuarioService;

import org.springframework.security.core.Authentication;
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

    //---------------------Metodo Registro-------------------------

    // 1. Mostrar el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Buscará registro.html
    }

    // 2. Procesar el formulario
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        // Si hay errores de formato 
        if (result.hasErrors()) {
            return "registro"; // Vuelve al formulario con los errores
        }

        try {
            //Se fuerza el rol a CLIENTE por seguridad, para que no puedan registrarse como admin
            usuario.setRol("CLIENTE");
            usuarioService.crear(usuario); 
            return "redirect:/login?exito";
        } catch (IllegalArgumentException e) {
            // Captura del "El correo ya está registrado" o "Cédula ya registrada"
            model.addAttribute("errorRegistro", e.getMessage());
            return "registro";
        }
    }
    // --------------------Recuperar contraseña --------------   
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperar(@RequestParam("email") String email, Model model) {
        System.out.println("--> Recibí el correo: " + email); 
        try {
            //Se genera una contraseña temporal y la encripta en la BD
            String passTemp = usuarioService.recuperarPassword(email);
            model.addAttribute("mensajeExito", "Contraseña restablecida. Tu nueva contraseña temporal es: " + passTemp); //Muestra la contraseña temporal en pantalla
            System.out.println("--> Contraseña generada: " + passTemp);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorRecuperar", e.getMessage());
        }
        return "recuperar";
    }
    //------------------------Perfil----------------------------
    //Muestra los datos del usuario que tiene la sesión activa
    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        String username = authentication.getName(); // Devuelve el username del usuario actual
        Usuario usuario = usuarioService.buscarPorUsername(username); 
        model.addAttribute("usuario", usuario);
        return "perfil";
    }
    //Procesa la edición del perfil del usuario logueado 
    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuario, Authentication authentication, Model model) {
        try {
            //Se busca al usuario original en la BD usando el username de la sesión
            Usuario usuarioActual = usuarioService.buscarPorUsername(authentication.getName());
            
            //Se fuerza al ID para que el usuario solo pueda ditar su propio perfil
            usuario.setId(usuarioActual.getId()); 
            usuarioService.actualizar(usuario);
            return "redirect:/?perfilActualizado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorPerfil", e.getMessage());
            return "perfil";
        }
    }
}
