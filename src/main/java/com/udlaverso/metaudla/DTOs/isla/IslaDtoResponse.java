package com.udlaverso.metaudla.DTOs.isla;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.enums.TipoLike;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Isla}
 */
@Value
public class IslaDtoResponse implements Serializable {
    Long id;
    String nombre;
    String descripcion;
    List<@Pattern(regexp = "^https?://.*", message = "La URL de imagen debe comenzar con http:// o https://") String> imagenes;
    List<@Pattern(regexp = "^https?://.*", message = "La URL de video debe comenzar con http:// o https://") String> videos;
    @Pattern(message = "El link de descarga debe comenzar con http:// o https://", regexp = "^https?://.*")
    String linkDescarga;
    Long autorId;
    String autorNombre;
    String autorCorreo;
    String autorFotoPerfilUrl;
    Rol autorRol;
    List<String> etiquetas;
    int visitas;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
    Long version;
    List<CategoriaDto> categorias;
    List<Long> favoritoIds;
    List<Integer> puntuacioneCalificacions;
    List<TipoLike> meGustaTipos;
    EstadoBasico estado;

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.Categoria}
     */
    @Value
    public static class CategoriaDto implements Serializable {
        Long id;
        String nombre;
        String descripcion;
    }
}