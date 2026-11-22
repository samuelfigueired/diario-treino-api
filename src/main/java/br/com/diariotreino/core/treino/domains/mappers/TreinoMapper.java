package br.com.diariotreino.core.treino.domains.mappers;

import br.com.diariotreino.core.domains.mappers.BaseMapper;
import br.com.diariotreino.core.domains.mappers.MapperConfigPadrao;
import br.com.diariotreino.core.entidades.Treino;
import br.com.diariotreino.core.entidades.TreinoExercicio;
import br.com.diariotreino.core.exercicio.domains.mappers.ExercicioMapper;
import br.com.diariotreino.core.treino.domains.dtos.TreinoExercicioOutDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoInDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoOutDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Os exercícios do treino não são mapeados do InDTO: o service resolve cada exercicioId
 * (precisa conferir se o exercício é visível para o usuário) em {@code validar}.
 */
@Mapper(config = MapperConfigPadrao.class, uses = ExercicioMapper.class)
public interface TreinoMapper extends BaseMapper<Treino, TreinoInDTO, TreinoOutDTO> {

    @Override
    @Mapping(target = "exercicios", ignore = true)
    Treino toEntidade(TreinoInDTO dto);

    @Override
    @Mapping(target = "exercicios", ignore = true)
    void updateEntidade(TreinoInDTO dto, @MappingTarget Treino entidade);

    @Mapping(target = "ultimaCargaKg", ignore = true)
    TreinoExercicioOutDTO toItemDto(TreinoExercicio item);

    /** Saída com a última carga de cada exercício (dados de outra tabela, buscados pelo service). */
    default TreinoOutDTO toDto(Treino treino, Map<Long, BigDecimal> ultimasCargas) {
        TreinoOutDTO dto = toDto(treino);
        dto.getExercicios().forEach(item -> item.setUltimaCargaKg(ultimasCargas.get(item.getExercicio().getId())));
        return dto;
    }
}
