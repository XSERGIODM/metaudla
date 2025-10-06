package com.udlaverso.metaudla.servicies.gestion_islas;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoCreate;
import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para la gestión de islas
 * Define las operaciones CRUD básicas para la entidad Isla
 */
public interface IGestionIsla {

    /**
     * Crear una nueva isla
     * @param islaDto Datos de la isla a crear
     * @return DTO de respuesta con la isla creada
     */
    IslaDtoResponse crearIsla(IslaDtoCreate islaDto);

    /**
     * Obtener una isla por su ID
     * @param id ID de la isla
     * @return Optional con el DTO de respuesta de la isla, vacío si no existe
     */
    Optional<IslaDtoResponse> obtenerIslaPorId(Long id);

    /**
     * Obtener todas las islas
     * @return Lista de todas las islas
     */
    List<IslaDtoResponse> obtenerTodasIslas();

    /**
     * Obtener islas con paginación
     * @param pageable Información de paginación
     * @return Página de islas
     */
    Page<IslaDtoResponse> obtenerIslasPaginadas(Pageable pageable);

    /**
     * Buscar islas por nombre
     * @param nombre Nombre a buscar (búsqueda parcial)
     * @return Lista de islas que contienen el nombre
     */
    List<IslaDtoResponse> buscarIslasPorNombre(String nombre);

    /**
     * Buscar islas por etiquetas
     * @param etiqueta Etiqueta a buscar
     * @return Lista de islas que contienen la etiqueta
     */
    List<IslaDtoResponse> buscarIslasPorEtiqueta(String etiqueta);

    /**
     * Obtener islas por categoría
     * @param categoriaId ID de la categoría
     * @return Lista de islas de la categoría
     */
    List<IslaDtoResponse> obtenerIslasPorCategoria(Long categoriaId);

    /**
     * Obtener islas por autor
     * @param autorId ID del autor
     * @return Lista de islas del autor
     */
    List<IslaDtoResponse> obtenerIslasPorAutor(Long autorId);

    /**
     * Actualizar una isla existente
     * @param id ID de la isla a actualizar
     * @param islaDto Datos actualizados de la isla
     * @return DTO de respuesta con la isla actualizada
     * @throws RuntimeException si la isla no existe
     */
    IslaDtoResponse actualizarIsla(Long id, IslaDtoCreate islaDto);

    /**
     * Eliminar una isla por su ID
     * @param id ID de la isla a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean eliminarIsla(Long id);

    /**
     * Incrementar el contador de visitas de una isla
     * @param id ID de la isla
     */
    void incrementarVisitas(Long id);

    /**
     * Verificar si existe una isla por ID
     * @param id ID de la isla
     * @return true si existe, false en caso contrario
     */
    boolean existeIsla(Long id);
}
