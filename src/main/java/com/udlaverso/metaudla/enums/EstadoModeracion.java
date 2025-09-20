package com.udlaverso.metaudla.enums;

import lombok.Getter;

@Getter
public enum EstadoModeracion {
    PENDIENTE("Pendiente"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");

    private final String value;

    EstadoModeracion(String value) {
        this.value = value;
    }
}