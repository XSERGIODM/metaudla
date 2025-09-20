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

    @Column
    String autor;

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

    // Métodos helper
    public void recalcularPromedio(List<Puntuacion> puntuaciones) {
        if (puntuaciones == null || puntuaciones.isEmpty()) {
            this.promedioPuntuacion = BigDecimal.ZERO;
        } else {
            double avg = puntuaciones.stream()
                .mapToInt(Puntuacion::getCalificacion)
                .average()
                .orElse(0.0);
            this.promedioPuntuacion = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void recalcularEstadisticas(List<MeGusta> meGustas, List<Comentario> comentarios, List<Favorito> favoritos) {
        // Recalcular Me Gustas y No Me Gustas
        if (meGustas != null) {
            this.totalMeGustas = (int) meGustas.stream()
                .filter(mg -> mg.getTipo() == TipoLike.ME_GUSTA)
                .count();

            this.totalNoMeGustas = (int) meGustas.stream()
                .filter(mg -> mg.getTipo() == TipoLike.NO_ME_GUSTA)
                .count();
        } else {
            this.totalMeGustas = 0;
            this.totalNoMeGustas = 0;
        }

        // Recalcular total de comentarios (solo comentarios principales, no respuestas)
        if (comentarios != null) {
            this.totalComentarios = (int) comentarios.stream()
                .filter(c -> c.getComentarioPadre() == null) // Solo comentarios raíz
                .count();
        } else {
            this.totalComentarios = 0;
        }

        // Recalcular total de favoritos
        if (favoritos != null) {
            this.totalFavoritos = favoritos.size();
        } else {
            this.totalFavoritos = 0;
        }
    }

    public void recalcularEstadisticas() {
        recalcularEstadisticas(this.meGustas, this.comentarios, this.favoritos);
    }

    // Métodos para obtener estadísticas específicas
    public int getTotalLikes() {
        return totalMeGustas;
    }

    public int getTotalDislikes() {
        return totalNoMeGustas;
    }

    public int getTotalComentarios() {
        return totalComentarios;
    }

    public int getTotalFavoritos() {
        return totalFavoritos;
    }

    public int getTotalInteracciones() {
        return totalMeGustas + totalNoMeGustas + totalComentarios + totalFavoritos;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
