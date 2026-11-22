package br.com.diariotreino.core.treino.domains.dtos;

import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioOutDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Exercício do treino devolvido pela API, com a última carga usada pelo usuário. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreinoExercicioOutDTO {

    private Long id;

    private Integer ordem;

    private ExercicioOutDTO exercicio;

    private Integer seriesPlanejadas;

    private Integer repeticoesPlanejadas;

    /** Carga da última série registrada deste exercício (nula se nunca foi feito ou é peso corporal). */
    private BigDecimal ultimaCargaKg;
}
