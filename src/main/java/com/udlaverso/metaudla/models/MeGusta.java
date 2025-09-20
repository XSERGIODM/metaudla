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
@Entity(name = "me_gusta")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "isla_id"}))
public class MeGusta {
    //atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime fechaCreacion;

    //relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "isla_id")
    Isla isla;

    //Enums
    @Enumerated(EnumType.STRING)
    TipoLike tipo;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
