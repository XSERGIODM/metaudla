package com.udlaverso.metaudla.models;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;

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
@Entity(name = "usuario")
public class Usuario {


    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String nombre;
    @Column
    String username;
    @Column
    String correo;
    @Column
    String contrasena;
    @Column
    String fotoPerfilUrl;

    //Enums
    @Enumerated(EnumType.STRING)
    EstadoBasico estado;
    @Enumerated(EnumType.STRING)
    Rol rol;

    //relaciones
    @OneToMany
    List<Categoria> categorias;
    @OneToMany
    List<Puntuacion> puntuaciones;
    @OneToMany
    List<MeGusta> meGustas;
    @OneToMany
    List<Comentario> comentarios;
    @OneToMany
    List<Favorito> favoritos;
}