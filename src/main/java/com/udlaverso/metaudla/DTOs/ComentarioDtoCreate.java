package com.udlaverso.metaudla.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDtoCreate {

    @NotBlank(message = "El contenido del comentario no puede estar vacío")
    private String contenido;

    @NotNull(message = "El ID de la isla es obligatorio")
    private Long islaId;

    private Long comentarioPadreId; // Opcional para respuestas anidadas
}