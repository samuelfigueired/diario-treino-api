package br.com.diariotreino.core.exercicio.services;

import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioInDTO;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioOutDTO;

import java.util.List;

public interface ExercicioService {

    /** Catálogo padrão mais os exercícios personalizados do usuário logado. */
    List<ExercicioOutDTO> listar();

    /** Cria um exercício personalizado, visível só para o usuário logado. */
    ExercicioOutDTO criar(ExercicioInDTO dto);
}
