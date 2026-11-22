package br.com.diariotreino.core.exercicio.domains.mappers;

import br.com.diariotreino.core.domains.mappers.BaseMapper;
import br.com.diariotreino.core.domains.mappers.MapperConfigPadrao;
import br.com.diariotreino.core.entidades.Exercicio;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioInDTO;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioOutDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfigPadrao.class)
public interface ExercicioMapper extends BaseMapper<Exercicio, ExercicioInDTO, ExercicioOutDTO> {

    @Override
    @Mapping(target = "personalizado", expression = "java(entidade.getUsuarioId() != null)")
    ExercicioOutDTO toDto(Exercicio entidade);
}
