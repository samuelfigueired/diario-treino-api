package br.com.diariotreino.core.execucao.domains.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Série devolvida pela API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieOutDTO {

    private Long exercicioId;

    private Integer numero;

    private BigDecimal cargaKg;

    private Integer repeticoes;

    private Boolean concluida;
}
