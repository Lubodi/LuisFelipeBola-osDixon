package com.lubodi.alpha.Enum;

public enum Niveles {
    NIVEL1(1),
    NIVEL2(2),
    NIVEL3(3),
    NIVEL4(4);

    private final int valor;

    Niveles(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
