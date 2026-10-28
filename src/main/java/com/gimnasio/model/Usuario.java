package com.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El username es requerido")
    @Column(unique = true)
    private String username;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es requerido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La cédula es requerida")
    @Column(unique = true)
    private String cedula;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    //@NotBlank(message = "El rol es requerido")
    private String rol; // "ADMIN" (Entrenador) o "CLIENTE"

    private boolean activo = true; // Para el borrado lógico

    // Constructores
    public Usuario() {}

    public Usuario(String nombre, String username, String email, String cedula, String password, String rol) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.cedula = cedula;
        this.password = password;
        this.rol = rol;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}