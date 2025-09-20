package com.udlaverso.metaudla.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comentario")
public class Comentario {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String contenido;
    @Column
    String fechaCreacion;


    //relaciones
    @ManyToOne
    Usuario usuario;
    @ManyToOne
    Isla isla;
}
