package com.udlaverso.metaudla.enums;

import lombok.Getter;

@Getter
public enum TipoLike {
    ME_GUSTA("Me gusta"),
    NO_ME_GUSTA("No me gusta");

    private final String value;

    TipoLike(String value) {
        this.value = value;
    }
}
