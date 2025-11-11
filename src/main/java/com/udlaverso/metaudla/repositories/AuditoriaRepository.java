package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.Auditoria;
import com.udlaverso.metaudla.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long>, JpaSpecificationExecutor<Auditoria> {

    // Métodos personalizados para consultas de auditoría comunes

    // Buscar por usuario
    List<Auditoria> findByUsuario(Usuario usuario);

    // Buscar por endpoint
    List<Auditoria> findByEndpoint(String endpoint);

    // Buscar por método HTTP
    List<Auditoria> findByMetodoHttp(String metodoHttp);

    // Buscar por IP del cliente
    List<Auditoria> findByIpCliente(String ipCliente);

    // Buscar por código de respuesta
    List<Auditoria> findByCodigoRespuesta(Integer codigoRespuesta);

    // Buscar por éxito
    List<Auditoria> findByExito(Boolean exito);

    // Buscar por rango de fechas
    List<Auditoria> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Combinaciones comunes

    // Buscar por usuario y rango de fechas
    List<Auditoria> findByUsuarioAndTimestampBetween(Usuario usuario, LocalDateTime start, LocalDateTime end);

    // Buscar por endpoint y rango de fechas
    List<Auditoria> findByEndpointAndTimestampBetween(String endpoint, LocalDateTime start, LocalDateTime end);

    // Buscar por endpoint y método HTTP
    List<Auditoria> findByEndpointAndMetodoHttp(String endpoint, String metodoHttp);

    // Consulta JPQL para buscar por ID de usuario y rango de fechas (evita cargar la entidad Usuario completa)
    @Query("SELECT a FROM auditoria a WHERE a.usuario.id = :usuarioId AND a.timestamp BETWEEN :start AND :end")
    List<Auditoria> findByUsuarioIdAndTimestampBetween(@Param("usuarioId") Long usuarioId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Consulta JPQL para buscar auditorías fallidas en un rango de fechas
    @Query("SELECT a FROM auditoria a WHERE a.exito = false AND a.timestamp BETWEEN :start AND :end ORDER BY a.timestamp DESC")
    List<Auditoria> findFailedAuditsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Consulta JPQL para contar auditorías por endpoint en un rango de fechas
    @Query("SELECT COUNT(a) FROM auditoria a WHERE a.endpoint = :endpoint AND a.timestamp BETWEEN :start AND :end")
    Long countByEndpointAndTimestampBetween(@Param("endpoint") String endpoint, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}