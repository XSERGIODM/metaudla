package com.udlaverso.metaudla.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaDtoResponse {
    private Long id;
    private String usuarioNombre;
    private String endpoint;
    private String metodoHttp;
    private LocalDateTime timestamp;
    private Integer codigoRespuesta;
    private Boolean exito;
    private String mensajeError;
}