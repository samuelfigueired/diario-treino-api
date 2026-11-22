package br.com.diariotreino.core.treino.domains.dtos;

import br.com.diariotreino.core.entidades.enums.DiaSemana;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Treino devolvido pela API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreinoOutDTO {

    private Long id;

    private String letra;

    private String nome;

    private List<DiaSemana> diasDaSemana;

    private Integer duracaoEstimadaMin;

    private List<TreinoExercicioOutDTO> exercicios;
}
