package br.com.diariotreino.core.execucao.domains.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Série feita durante o treino. Carga vazia = peso corporal. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieInDTO {

    @NotNull
    private Long exercicioId;

    @NotNull
    @Positive
    private Integer numero;

    @PositiveOrZero(message = "A carga não pode ser negativa.")
    @Digits(integer = 5, fraction = 2, message = "Use no máximo duas casas decimais na carga.")
    private BigDecimal cargaKg;

    @NotNull(message = "Informe as repetições.")
    @Positive(message = "Informe as repetições.")
    private Integer repeticoes;

    private Boolean concluida;
}
