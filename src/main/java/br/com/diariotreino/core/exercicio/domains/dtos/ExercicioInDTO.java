package br.com.diariotreino.core.exercicio.domains.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Cadastro de exercício personalizado. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioInDTO {

    @NotBlank(message = "Informe o nome do exercício.")
    @Size(max = 120)
    private String nome;

    @NotBlank(message = "Informe o grupo muscular.")
    @Size(max = 60)
    private String grupoMuscular;
}
