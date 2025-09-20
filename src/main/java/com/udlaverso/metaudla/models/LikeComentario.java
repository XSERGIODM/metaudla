package com.udlaverso.metaudla.models;

import com.udlaverso.metaudla.enums.TipoLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "like_comentario")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "comentario_id"}))
public class LikeComentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "comentario_id")
    Comentario comentario;

    @Enumerated(EnumType.STRING)
    TipoLike tipo;

    @Column(nullable = false)
    LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}