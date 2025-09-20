package com.udlaverso.metaudla.models;

import java.util.List;

import com.udlaverso.metaudla.enums.EstadoBasico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "isla")
public class Isla {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String nombre;
    @Column
    String descripcion;
    @Column
    List<String> imagenes;
    @Column
    List<String> videos;
    @Column
    String linkDescarga;
    @Column
    String autor;
    @Column
    List<String> etiquetas;
    @Column
    int visitas;
    @Column
    String fechaCreacion;
    @Column
    String fechaActualizacion;
    @Column
    String version;

    //relaciones
    @ManyToMany
    List<Categoria> categorias;
    @OneToMany
    List<Puntuacion> puntuaciones;
    @OneToMany
    List<MeGusta> meGustas;
    @OneToMany
    List<Comentario> comentarios;
    @OneToMany
    List<Favorito> favoritos;

    //Enums
    @Enumerated(EnumType.STRING)
    EstadoBasico estado;

}
