package com.udlaverso.metaudla.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Puntuacion {

    // Atributos de la clase
    int id;
    int calificacion;

    // Relaciones
    Usuario usuario;
    Isla isla;
}
