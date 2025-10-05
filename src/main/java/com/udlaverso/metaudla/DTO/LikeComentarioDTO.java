package com.udlaverso.metaudla.DTO;

import com.udlaverso.metaudla.enums.TipoLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class LikeComentarioDTO {

    Long id;
    TipoLike tipo;
    LocalDateTime fechaCreacion;

    // Referencias mínimas para evitar ciclos
    Long usuarioId;
    String usuarioNombre;
    Long comentarioId;
    String comentarioContenido;
}