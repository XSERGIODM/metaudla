package com.udlaverso.metaudla.models;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {


    // Atributos de la clase
    int id;
    String nombre;
    String username;
    String correo;
    String contrasena;
    String fotoPerfil;

    //Enums
    EstadoBasico estado;
    Rol rol;
}