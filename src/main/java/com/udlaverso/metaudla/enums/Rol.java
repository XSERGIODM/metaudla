package com.udlaverso.metaudla.enums;

import lombok.Getter;

@Getter
public enum Rol {

    ESTUDIANTE("Estudiante"),
    PROFESOR("Profesor"),
    ADMINISTRADOR("Administrador");

    private final String value;

    Rol(String value) {
        this.value = value;
    }
}
