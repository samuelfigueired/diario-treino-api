package br.com.diariotreino.core.exercicio.services.impl;

import br.com.diariotreino.core.entidades.Exercicio;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioInDTO;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioOutDTO;
import br.com.diariotreino.core.exercicio.domains.mappers.ExercicioMapper;
import br.com.diariotreino.core.exercicio.repositorys.ExercicioRepository;
import br.com.diariotreino.core.exercicio.services.ExercicioService;
import br.com.diariotreino.core.util.CurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExercicioServicePadrao implements ExercicioService {

    private final ExercicioRepository repository;
    private final ExercicioMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ExercicioOutDTO> listar() {
        return mapper.toDtoList(repository.visiveis(CurrentUser.usuarioId()));
    }

    @Override
    @Transactional
    public ExercicioOutDTO criar(ExercicioInDTO dto) {
        Exercicio exercicio = mapper.toEntidade(dto);
        exercicio.setNome(exercicio.getNome().trim());
        exercicio.setGrupoMuscular(exercicio.getGrupoMuscular().trim());
        exercicio.setUsuarioId(CurrentUser.usuarioId());
        return mapper.toDto(repository.save(exercicio));
    }
}
