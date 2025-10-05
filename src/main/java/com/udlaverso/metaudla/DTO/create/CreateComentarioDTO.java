package com.udlaverso.metaudla.DTO.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateComentarioDTO {

    @NotBlank(message = "El contenido del comentario es obligatorio")
    String contenido;

    // Para respuestas anidadas (opcional)
    Long comentarioPadreId;

    @NotBlank(message = "El ID del usuario es obligatorio")
    Long usuarioId;

    @NotBlank(message = "El ID de la isla es obligatorio")
    Long islaId;
}