package com.udlaverso.metaudla.models;

import java.util.List;

import com.udlaverso.metaudla.enums.EstadoBasico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Isla {

    // Atributos de la clase
    int id;
    String nombre;
    String descripcion;
    List<String> imagenes;
    List<String> videos;
    String linkDescarga;
    String autor;
    List<String> etiquetas;
    int visitas;
    String fechaCreacion;
    String fechaActualizacion;
    String version;

    //relaciones
    List<Categoria> categorias;

    //Enums
    EstadoBasico estado;

}
