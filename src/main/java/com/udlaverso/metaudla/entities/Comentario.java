package com.udlaverso.metaudla.models;

import com.udlaverso.metaudla.enums.EstadoModeracion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comentario")
public class Comentario {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String contenido;
    @Column(nullable = false)
    LocalDateTime fechaCreacion;

    // Respuestas anidadas
    @ManyToOne
    @JoinColumn(name = "comentario_padre_id")
    Comentario comentarioPadre;

    @OneToMany(mappedBy = "comentarioPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comentario> respuestas = new ArrayList<>();

    // Moderación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EstadoModeracion estadoModeracion = EstadoModeracion.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "moderador_id")
    Usuario moderador;

    @Column
    LocalDateTime fechaModeracion;

    // Likes en comentarios
    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true)
    List<LikeComentario> likes = new ArrayList<>();

    //relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "isla_id")
    Isla isla;

    // Métodos helper
    public boolean esRespuesta() {
        return comentarioPadre != null;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
