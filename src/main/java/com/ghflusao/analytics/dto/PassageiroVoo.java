package com.ghflusao.analytics.dto;

import com.ghflusao.model.SituacaoBilhete;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Passageiro e bilhete associados a um voo. */
public record PassageiroVoo(Integer idBilhete,
                            String nome,
                            String cpf,
                            LocalDateTime dataEmissao,
                            BigDecimal valorTotal,
                            SituacaoBilhete situacao) {

    public String cpfFormatado() {
        return Formatos.cpf(cpf);
    }

    public String dataFormatada() {
        return Formatos.dataHora(dataEmissao);
    }

    public String valorFormatado() {
        return Formatos.moeda(valorTotal);
    }

    public boolean cancelado() {
        return situacao == SituacaoBilhete.CANCELADO;
    }
}
