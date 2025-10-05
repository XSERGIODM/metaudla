package com.udlaverso.metaudla.DTO;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
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
public class UsuarioDTO {

    Long id;
    String nombre;
    String username;
    String correo;
    String fotoPerfilUrl;

    // Auditoría
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // Enums
    EstadoBasico estado;
    Rol rol;

    // Referencias mínimas para evitar ciclos - solo conteos e IDs básicos
    int totalFavoritos;
    int totalPuntuaciones;
    int totalMeGustas;
    int totalComentarios;
    int totalIslas;

    // Referencias básicas para auditoría
    Long createdById;
    String createdByNombre;
    Long updatedById;
    String updatedByNombre;
}