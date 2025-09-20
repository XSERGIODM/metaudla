package com.udlaverso.metaudla.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    // Atributos de la clase
    int id;
    String nombre;
    String descripcion;
    String estado;
}
