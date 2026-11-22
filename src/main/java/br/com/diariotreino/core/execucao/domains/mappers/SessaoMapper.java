package br.com.diariotreino.core.execucao.domains.mappers;

import br.com.diariotreino.core.domains.mappers.BaseMapper;
import br.com.diariotreino.core.domains.mappers.MapperConfigPadrao;
import br.com.diariotreino.core.entidades.SerieRegistrada;
import br.com.diariotreino.core.entidades.SessaoTreino;
import br.com.diariotreino.core.execucao.domains.dtos.SerieInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SerieOutDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoOutDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/** Nome e letra do treino e o vínculo série → sessão são preenchidos pelo service. */
@Mapper(config = MapperConfigPadrao.class)
public interface SessaoMapper extends BaseMapper<SessaoTreino, SessaoInDTO, SessaoOutDTO> {

    @Override
    @Mapping(target = "nomeTreino", ignore = true)
    @Mapping(target = "letraTreino", ignore = true)
    SessaoTreino toEntidade(SessaoInDTO dto);

    /** Uma sessão salva não é editada por inteiro: só os horários podem ser corrigidos. */
    @Override
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "treinoId", ignore = true)
    @Mapping(target = "nomeTreino", ignore = true)
    @Mapping(target = "letraTreino", ignore = true)
    void updateEntidade(SessaoInDTO dto, @MappingTarget SessaoTreino entidade);

    @Mapping(target = "sessao", ignore = true)
    @Mapping(target = "concluida", defaultValue = "true")
    SerieRegistrada toSerie(SerieInDTO dto);

    SerieOutDTO toSerieDto(SerieRegistrada serie);
}
