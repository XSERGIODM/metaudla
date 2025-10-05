package com.udlaverso.metaudla.DTO.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateFavoritoDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    Long usuarioId;

    @NotNull(message = "El ID de la isla es obligatorio")
    Long islaId;
}