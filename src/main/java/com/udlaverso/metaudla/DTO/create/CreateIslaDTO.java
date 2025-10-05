package com.udlaverso.metaudla.DTO.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateIslaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    String nombre;

    String descripcion;

    List<@Pattern(regexp = "^https?://.*", message = "La URL de imagen debe comenzar con http:// o https://") String> imagenes;
    List<@Pattern(regexp = "^https?://.*", message = "La URL de video debe comenzar con http:// o https://") String> videos;

    @Pattern(regexp = "^https?://.*", message = "El link de descarga debe comenzar con http:// o https://")
    String linkDescarga;

    @NotBlank(message = "El ID del autor es obligatorio")
    Long autorId;

    List<String> etiquetas;
    List<Long> categoriasIds;
}