package com.udlaverso.metaudla.DTO;

import com.udlaverso.metaudla.enums.EstadoModeracion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {

    Long id;
    String contenido;
    LocalDateTime fechaCreacion;

    // Información sobre si es respuesta
    boolean esRespuesta;
    Long comentarioPadreId;
    String comentarioPadreContenido;

    // Información de respuestas (sin recursión profunda)
    int totalRespuestas;
    List<Long> respuestasIds;

    // Moderación
    EstadoModeracion estadoModeracion;
    Long moderadorId;
    String moderadorNombre;
    LocalDateTime fechaModeracion;

    // Información de likes
    int totalLikes;
    List<Long> likesIds;

    // Información del usuario y isla (referencias mínimas)
    Long usuarioId;
    String usuarioNombre;
    Long islaId;
    String islaNombre;
}