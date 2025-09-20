package com.udlaverso.metaudla.models;

import com.udlaverso.metaudla.enums.TipoLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    //atributos de la clase
    int id;

    //relaciones
    Usuario usuario;
    Isla isla;

    //Enums
    TipoLike tipo;
}
