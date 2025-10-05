package com.udlaverso.metaudla.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoDTO {

    Long id;
    LocalDateTime fechaCreacion;

    // Referencias mínimas para evitar ciclos
    Long usuarioId;
    String usuarioNombre;
    Long islaId;
    String islaNombre;
}