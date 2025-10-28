package com.udlaverso.metaudla.DTOs;

import com.udlaverso.metaudla.enums.EstadoBasico;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Categoria}
 */
@Value
public class CategoriaDto implements Serializable {
    Long id;
    String nombre;
    String descripcion;
    EstadoBasico estado;
    List<IslaDto> islas;

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.Isla}
     */
    @Value
    public static class IslaDto implements Serializable {
        Long id;
        String nombre;
        String descripcion;
        List<String> imagenes;
        List<String> videos;
        String linkDescarga;
        Long autorId;
        String autorNombre;
        List<String> etiquetas;
        int visitas;
        LocalDateTime fechaCreacion;
        LocalDateTime fechaActualizacion;
        Long version;
        EstadoBasico estado;
    }
}