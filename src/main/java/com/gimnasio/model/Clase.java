package com.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "clases") //Define el nombre de la tabla en la BD
public class Clase {

    @Id //Llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //La BD egnera un ID auntomático
    private Long id;

    @NotBlank(message = "El nombre o tipo de clase es requerido")//Valida que el espacio no este vacio
    private String nombre;

    // Relación con el usuario que es entrenador (ADMIN)
    @ManyToOne //Muchas clases pueder ser dadas por un solo entrenador
    @JoinColumn(name = "entrenador_id", nullable = false) //Crea la llave foranea 
    @NotNull(message = "Debe seleccionar un entrenador")
    private Usuario entrenador;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "La fecha y hora de inicio son requeridas")
    private LocalDateTime fechaInicio;

    // La fecha de fin se calcula automáticamente, no se pide en el formulario
    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @NotNull(message = "El cupo máximo es requerido")
    @Min(value = 1, message = "El cupo debe ser al menos 1") //Min valida que el cupo no sea 0 ni negativo
    private Integer cupoMaximo;

    // Método que se ejecuta automáticamente antes de guardar en la BD
    @PrePersist
    @PreUpdate
    public void calcularFechaFin() {
        if (this.fechaInicio != null) {
            //Se toma la hora de inicio y se le suma 1h
            this.fechaFin = this.fechaInicio.plusHours(1);
        }
    }

    // Constructores
    public Clase() {}

    public Clase(String nombre, Usuario entrenador, LocalDateTime fechaInicio, Integer cupoMaximo) {
        this.nombre = nombre;
        this.entrenador = entrenador;
        this.fechaInicio = fechaInicio;
        this.cupoMaximo = cupoMaximo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Usuario getEntrenador() { return entrenador; }
    public void setEntrenador(Usuario entrenador) { this.entrenador = entrenador; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }
}
