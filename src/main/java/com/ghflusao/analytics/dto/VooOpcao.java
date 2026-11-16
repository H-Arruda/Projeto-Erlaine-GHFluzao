package com.ghflusao.analytics.dto;

/** Item da lista de seleção de voos. */
public record VooOpcao(Integer id, String codigoVoo, String empresa) {

    public String rotulo() {
        return codigoVoo + " · " + empresa;
    }
}
