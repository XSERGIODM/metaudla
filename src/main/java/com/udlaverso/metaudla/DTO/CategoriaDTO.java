package com.udlaverso.metaudla.DTO;

import com.udlaverso.metaudla.enums.EstadoBasico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {

    Long id;
    String nombre;
    String descripcion;
    EstadoBasico estado;

    // Referencias mínimas para evitar ciclos - solo IDs y nombres básicos
    List<Long> islasIds;
    List<String> islasNombres;
}