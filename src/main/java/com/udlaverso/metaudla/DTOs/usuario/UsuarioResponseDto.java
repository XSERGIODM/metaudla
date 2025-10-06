package com.udlaverso.metaudla;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.enums.TipoLike;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Usuario}
 */
@Value
public class UsuarioResponseDto implements Serializable {
    Long id;
    String nombre;
    String username;
    String correo;
    String fotoPerfilUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long createdById;
    String createdByNombre;
    EstadoBasico createdByEstado;
    Rol createdByRol;
    Long updatedById;
    String updatedByNombre;
    EstadoBasico estado;
    Rol rol;
    List<FavoritoResponseDto> favoritos;
    List<PuntuacionDto> puntuaciones;
    List<MeGustaDto> meGustas;
    List<IslaDto> islas;

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.Favorito}
     */
    @Value
    public static class FavoritoResponseDto implements Serializable {
        Long id;
        LocalDateTime fechaCreacion;
        Long usuarioId;
        String usuarioNombre;
        Long islaId;
        String islaNombre;
        String islaDescripcion;
    }

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.Puntuacion}
     */
    @Value
    public static class PuntuacionDto implements Serializable {
        Long id;
        int calificacion;
        LocalDateTime fechaCreacion;
        Long usuarioId;
        String usuarioNombre;
        Long islaId;
        String islaNombre;
        String islaDescripcion;
    }

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.MeGusta}
     */
    @Value
    public static class MeGustaDto implements Serializable {
        Long id;
        LocalDateTime fechaCreacion;
        Long usuarioId;
        String usuarioNombre;
        Long islaId;
        String islaNombre;
        String islaDescripcion;
        TipoLike tipo;
    }

    /**
     * DTO for {@link com.udlaverso.metaudla.entities.Isla}
     */
    @Value
    public static class IslaDto implements Serializable {
        Long id;
        String nombre;
        String descripcion;
    }
}