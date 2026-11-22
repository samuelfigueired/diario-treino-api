package br.com.diariotreino.core.domains.mappers;

import org.mapstruct.MappingTarget;

/**
 * Contrato comum dos mappers (MapStruct gera a implementação).
 *
 * @param <E>   entidade JPA
 * @param <IN>  DTO de entrada (o que o app envia)
 * @param <OUT> DTO de saída (o que a API devolve)
 */
public interface BaseMapper<E, IN, OUT> extends SaidaMapper<E, OUT> {

    /** Cria uma entidade nova a partir do DTO de entrada. id, usuarioId e criadoEm ficam por conta do JPA. */
    E toEntidade(IN dto);

    /** Copia o DTO de entrada para uma entidade existente (atualização). Campos nulos no DTO são ignorados. */
    void updateEntidade(IN dto, @MappingTarget E entidade);
}
