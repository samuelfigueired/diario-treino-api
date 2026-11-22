package br.com.diariotreino.core.exercicio.domains.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Exercício devolvido pela API. personalizado = criado pelo usuário (não é do catálogo). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioOutDTO {

    private Long id;

    private String nome;

    private String grupoMuscular;

    private boolean personalizado;
}
