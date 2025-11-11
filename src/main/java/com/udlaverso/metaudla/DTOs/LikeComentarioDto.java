package com.udlaverso.metaudla.DTOs;

import com.udlaverso.metaudla.enums.TipoLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeComentarioDto {

    private Long id;
    private TipoLike tipo;
    private LocalDateTime fechaCreacion;

    // Usuario
    private Long usuarioId;
    private String usuarioNombre;

    // Comentario
    private Long comentarioId;
}