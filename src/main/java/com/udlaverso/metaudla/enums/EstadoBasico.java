package com.udlaverso.metaudla.enums;

import lombok.Getter;

@Getter
public enum EstadoBasico {
    HABILITADO("Habilitado"),
    DESHABILITADO("Deshabilitado");

    private final String value;

    EstadoBasico(String value) {
        this.value = value;
    }
}
