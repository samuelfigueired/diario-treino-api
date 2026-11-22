package br.com.diariotreino.core.treino.domains.dtos;

import br.com.diariotreino.core.entidades.enums.DiaSemana;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Cadastro/edição de treino. Na edição, exercicios nulo mantém os exercícios atuais. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreinoInDTO {

    @NotBlank(message = "Informe a letra do treino.")
    @Size(max = 3, message = "Use até 3 caracteres na letra.")
    private String letra;

    @NotBlank(message = "Informe o nome do treino.")
    @Size(max = 80)
    private String nome;

    private List<DiaSemana> diasDaSemana;

    @Positive
    private Integer duracaoEstimadaMin;

    @Valid
    private List<TreinoExercicioInDTO> exercicios;
}
