package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MainController{

    // Esta ruta es la que muestra el formulario de login
    @GetMapping("/login")
    public String login() {
        return "login"; // Busca el archivo login.html en templates
    }

    
    @GetMapping("/")
    public String inicio() {
        return "index"; 
    }
    
}
