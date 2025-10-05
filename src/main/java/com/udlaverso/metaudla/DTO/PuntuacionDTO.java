package com.udlaverso.metaudla.DTO;

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
public class PuntuacionDTO {

    Long id;

    @Min(1)
    @Max(5)
    int calificacion;

    LocalDateTime fechaCreacion;

    // Referencias mínimas para evitar ciclos
    Long usuarioId;
    String usuarioNombre;
    Long islaId;
    String islaNombre;
}