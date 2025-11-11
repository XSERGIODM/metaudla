package com.udlaverso.metaudla.entities;

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
@Entity(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    Usuario usuario;

    @Column(nullable = false)
    String endpoint;

    @Column(nullable = false)
    String metodoHttp;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @Column(nullable = false)
    String ipCliente;

    @Column(nullable = false)
    Integer codigoRespuesta;

    @Column(nullable = false)
    Long tiempoRespuesta;

    @Column
    String userAgent;

    @Column(length = 2000)
    String parametrosConsulta;

    @Column(length = 5000)
    String cuerpoSolicitud;

    @Column(nullable = false)
    Boolean exito;

    @Column(length = 1000)
    String mensajeError;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}