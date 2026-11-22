package br.com.diariotreino.core.treino.domains.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Exercício do treino com o planejado. A ordem é a posição na lista. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreinoExercicioInDTO {

    @NotNull(message = "Escolha o exercício.")
    private Long exercicioId;

    @NotNull(message = "Informe as séries.")
    @Positive(message = "Informe as séries.")
    @Max(value = 20, message = "Use no máximo 20 séries.")
    private Integer seriesPlanejadas;

    @NotNull(message = "Informe as repetições.")
    @Positive(message = "Informe as repetições.")
    @Max(value = 200, message = "Use no máximo 200 repetições.")
    private Integer repeticoesPlanejadas;
}
