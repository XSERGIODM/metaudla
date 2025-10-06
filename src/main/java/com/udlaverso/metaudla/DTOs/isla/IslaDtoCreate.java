package com.udlaverso.metaudla.DTOs.isla;

import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Isla}
 */
@Value
public class IslaDtoCreate implements Serializable {
    String nombre;
    String descripcion;
    List<String> imagenes;
    List<String> videos;
    @Pattern(message = "El link de descarga debe comenzar con http:// o https://", regexp = "^https?://.*")
    String linkDescarga;
    Long autorId;
    List<String> etiquetas;
    List<Long> categoriaIds;
}