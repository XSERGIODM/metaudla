package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.AuditoriaDtoResponse;
import com.udlaverso.metaudla.entities.Auditoria;
import com.udlaverso.metaudla.repositories.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    @GetMapping
    public ResponseEntity<Page<AuditoriaDtoResponse>> getAuditorias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String usuarioNombre,
            @RequestParam(required = false) String endpoint,
            @RequestParam(required = false) String metodoHttp,
            @RequestParam(required = false) Integer codigoRespuesta,
            @RequestParam(required = false) Boolean exito,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String busqueda) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Auditoria> spec = Specification.where(null);

        if (usuarioNombre != null && !usuarioNombre.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("usuario").get("nombre")), "%" + usuarioNombre.toLowerCase() + "%"));
        }

        if (endpoint != null && !endpoint.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("endpoint")), "%" + endpoint.toLowerCase() + "%"));
        }

        if (metodoHttp != null && !metodoHttp.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.equal(cb.lower(root.get("metodoHttp")), metodoHttp.toLowerCase()));
        }

        if (codigoRespuesta != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("codigoRespuesta"), codigoRespuesta));
        }

        if (exito != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("exito"), exito));
        }

        if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
            LocalDateTime desde = LocalDateTime.parse(fechaDesde);
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("timestamp"), desde));
        }

        if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
            LocalDateTime hasta = LocalDateTime.parse(fechaHasta);
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("timestamp"), hasta));
        }

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            String searchTerm = "%" + busqueda.toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                cb.or(
                    cb.like(cb.lower(root.get("usuario").get("nombre")), searchTerm),
                    cb.like(cb.lower(root.get("endpoint")), searchTerm),
                    cb.like(cb.lower(root.get("mensajeError")), searchTerm)
                ));
        }

        Page<Auditoria> auditorias = auditoriaRepository.findAll(spec, pageable);
        Page<AuditoriaDtoResponse> response = auditorias.map(this::convertToDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<AuditoriaDtoResponse>> getAuditoriasRecientes(
            @RequestParam(defaultValue = "10") int limit) {

        Pageable pageable = PageRequest.of(0, limit, Sort.by("timestamp").descending());
        List<Auditoria> auditorias = auditoriaRepository.findAll(pageable).getContent();

        List<AuditoriaDtoResponse> response = auditorias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private AuditoriaDtoResponse convertToDto(Auditoria auditoria) {
        return new AuditoriaDtoResponse(
                auditoria.getId(),
                auditoria.getUsuario() != null ? auditoria.getUsuario().getNombre() : "Sistema",
                auditoria.getEndpoint(),
                auditoria.getMetodoHttp(),
                auditoria.getTimestamp(),
                auditoria.getCodigoRespuesta(),
                auditoria.getExito(),
                auditoria.getMensajeError()
        );
    }
}