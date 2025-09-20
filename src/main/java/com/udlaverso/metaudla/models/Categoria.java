package com.udlaverso.metaudla.models;

import com.udlaverso.metaudla.enums.EstadoBasico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "categoria")
public class Categoria {

    // Atributos de la clase

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String nombre;
    @Column
    String descripcion;

    // enums
    @Enumerated(EnumType.STRING)
    EstadoBasico estado;

    //Relaciones
    @ManyToMany
    List<Isla> islas;
    
}
