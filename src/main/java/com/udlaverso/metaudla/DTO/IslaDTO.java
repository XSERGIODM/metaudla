package com.udlaverso.metaudla.DTO;

import com.udlaverso.metaudla.enums.EstadoBasico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class IslaDTO {

    Long id;
    String nombre;
    String descripcion;

    // URLs de medios
    List<String> imagenes;
    List<String> videos;
    String linkDescarga;

    // Información del autor (referencia mínima)
    Long autorId;
    String autorNombre;

    // Etiquetas y categorías
    List<String> etiquetas;
    List<Long> categoriasIds;
    List<String> categoriasNombres;

    // Estadísticas básicas
    int visitas;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
    Long version;

    // Estadísticas calculadas
    BigDecimal promedioPuntuacion;
    int totalMeGustas;
    int totalNoMeGustas;
    int totalComentarios;
    int totalFavoritos;

    // Estado
    EstadoBasico estado;
}