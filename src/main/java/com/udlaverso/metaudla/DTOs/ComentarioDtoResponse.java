package com.udlaverso.metaudla.DTOs;

import com.udlaverso.metaudla.enums.EstadoModeracion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDtoResponse {

    private Long id;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private EstadoModeracion estadoModeracion;
    private LocalDateTime fechaModeracion;

    // Usuario
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioUsername;

    // Isla
    private Long islaId;
    private String islaNombre;

    // Respuestas anidadas
    private Long comentarioPadreId;
    private List<ComentarioDtoResponse> respuestas;

    // Likes
    private int likesCount;
    private int dislikesCount;
    private String userLikeType; // ME_GUSTA, NO_ME_GUSTA o null
}