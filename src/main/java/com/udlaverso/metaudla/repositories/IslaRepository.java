package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.models.Isla;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IslaRepository extends JpaRepository<Isla, Long> {

    // Búsquedas básicas
    List<Isla> findByNombreContainingIgnoreCase(String nombre);
    List<Isla> findByAutor(String autor);
    List<Isla> findByEstado(EstadoBasico estado);

    // Búsqueda por etiquetas
    @Query("SELECT DISTINCT i FROM isla i JOIN i.etiquetas e WHERE e LIKE CONCAT('%', :etiqueta, '%')")
    List<Isla> findByEtiquetasContaining(@Param("etiqueta") String etiqueta);

    // Búsqueda con estadísticas (eager loading)
    @Query("SELECT i FROM isla i LEFT JOIN FETCH i.meGustas LEFT JOIN FETCH i.comentarios LEFT JOIN FETCH i.favoritos LEFT JOIN FETCH i.puntuaciones WHERE i.id = :id")
    Optional<Isla> findByIdWithStats(@Param("id") Long id);

    // Top listas por estadísticas
    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY (i.totalMeGustas + i.totalNoMeGustas + i.totalComentarios + i.totalFavoritos) DESC")
    List<Isla> findTopByTotalInteracciones(Pageable pageable);

    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY i.promedioPuntuacion DESC")
    List<Isla> findTopByPromedioPuntuacion(Pageable pageable);

    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY i.totalComentarios DESC")
    List<Isla> findTopByComentarios(Pageable pageable);

    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY i.totalFavoritos DESC")
    List<Isla> findTopByFavoritos(Pageable pageable);

    // Conteos
    long countByEstado(EstadoBasico estado);

    // Sumas para estadísticas globales
    @Query("SELECT SUM(i.visitas) FROM isla i WHERE i.estado = 'HABILITADO'")
    long sumAllVisitas();

    @Query("SELECT SUM(i.totalMeGustas + i.totalNoMeGustas + i.totalComentarios + i.totalFavoritos) FROM isla i WHERE i.estado = 'HABILITADO'")
    long sumAllInteracciones();

    // Actualización masiva de estadísticas
    @Modifying
    @Query("UPDATE isla i SET " +
            "i.totalMeGustas = (SELECT COUNT(mg) FROM me_gusta mg WHERE mg.isla = i AND mg.tipo = 'ME_GUSTA'), " +
            "i.totalNoMeGustas = (SELECT COUNT(mg) FROM me_gusta mg WHERE mg.isla = i AND mg.tipo = 'NO_ME_GUSTA'), " +
            "i.totalComentarios = (SELECT COUNT(c) FROM comentario c WHERE c.isla = i AND c.comentarioPadre IS NULL), " +
            "i.totalFavoritos = (SELECT COUNT(f) FROM favorito f WHERE f.isla = i), " +
            "i.promedioPuntuacion = (" +
            "SELECT COALESCE(AVG(CAST(p.calificacion AS DOUBLE)), 0) FROM puntuacion p WHERE p.isla = i)")
    void actualizarEstadisticasGlobales();

    // Búsquedas avanzadas
    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' AND " +
           "(LOWER(i.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.descripcion) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.autor) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Isla> buscarPorTexto(@Param("query") String query);

    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' AND " +
           "i.promedioPuntuacion >= :minRating ORDER BY i.promedioPuntuacion DESC")
    List<Isla> findByMinRating(@Param("minRating") double minRating);

    // Islas recientes
    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY i.fechaCreacion DESC")
    List<Isla> findMasRecientes(Pageable pageable);

    // Islas más visitadas
    @Query("SELECT i FROM isla i WHERE i.estado = 'HABILITADO' ORDER BY i.visitas DESC")
    List<Isla> findMasVisitadas(Pageable pageable);

    // Paginación
    Page<Isla> findAllByEstado(EstadoBasico estado, Pageable pageable);
}