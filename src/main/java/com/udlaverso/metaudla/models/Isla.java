package com.udlaverso.metaudla.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.TipoLike;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "isla")
public class Isla {

    // Atributos de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String nombre;
    @Column
    String descripcion;

    @ElementCollection
    @CollectionTable(name = "isla_imagenes", joinColumns = @JoinColumn(name = "isla_id"))
    @Column(name = "url_imagen")
    List<@Pattern(regexp = "^https?://.*", message = "La URL de imagen debe comenzar con http:// o https://") String> imagenes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "isla_videos", joinColumns = @JoinColumn(name = "isla_id"))
    @Column(name = "url_video")
    List<@Pattern(regexp = "^https?://.*", message = "La URL de video debe comenzar con http:// o https://") String> videos = new ArrayList<>();

    @Column
    @Pattern(regexp = "^https?://.*", message = "El link de descarga debe comenzar con http:// o https://")
    String linkDescarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="autor_id")
    Usuario autor;

    @ElementCollection
    @CollectionTable(name = "isla_etiquetas", joinColumns = @JoinColumn(name = "isla_id"))
    @Column(name = "etiqueta")
    List<String> etiquetas = new ArrayList<>();

    @Column(nullable = false)
    int visitas = 0;

    @Column(nullable = false)
    LocalDateTime fechaCreacion;

    @Column
    LocalDateTime fechaActualizacion;

    @Version
    @Column
    Long version;

    @Column(precision = 3, scale = 2)
    BigDecimal promedioPuntuacion;

    // Estadísticas calculadas
    @Column(nullable = false)
    int totalMeGustas = 0;

    @Column(nullable = false)
    int totalNoMeGustas = 0;

    @Column(nullable = false)
    int totalComentarios = 0;

    @Column(nullable = false)
    int totalFavoritos = 0;

    //relaciones
    @ManyToMany
    List<Categoria> categorias = new ArrayList<>();
    @OneToMany(mappedBy = "isla")
    List<Favorito> favoritos = new ArrayList<>();
    @OneToMany(mappedBy = "isla")
    List<Puntuacion> puntuaciones = new ArrayList<>();
    @OneToMany(mappedBy = "isla")
    List<MeGusta> meGustas = new ArrayList<>();
    @OneToMany(mappedBy = "isla")
    List<Comentario> comentarios = new ArrayList<>();

    //Enums
    @Enumerated(EnumType.STRING)
    EstadoBasico estado;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
