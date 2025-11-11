package com.udlaverso.metaudla.DTOs.isla;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Isla}
 */
@Value
public class IslaDtoCreate implements Serializable {
    String nombre;
    String descripcion;
    List<@Pattern(regexp = "^https?://.*", message = "La URL de imagen debe comenzar con http:// o https://") String> imagenes;
    List<@Pattern(regexp = "^https?://.*", message = "La URL de video debe comenzar con http:// o https://") String> videos;
    @Pattern(message = "El link de descarga debe comenzar con http:// o https://", regexp = "^https?://.*")
    String linkDescarga;
    @Size(max = 10, message = "No se permiten más de 10 imágenes")
    List<MultipartFile> imagenesArchivos;
    @Size(max = 5, message = "No se permiten más de 5 videos")
    List<MultipartFile> videosArchivos;
    MultipartFile archivoDescarga;
    Long autorId;
    List<String> etiquetas;
    List<Long> categoriaIds;
}