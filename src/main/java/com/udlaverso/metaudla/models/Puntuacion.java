package com.udlaverso.metaudla.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "puntuacion")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "isla_id"}))
public class Puntuacion {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    @Min(1)
    @Max(5)
    int calificacion;

    @Column(nullable = false)
    LocalDateTime fechaCreacion;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "isla_id")
    Isla isla;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
