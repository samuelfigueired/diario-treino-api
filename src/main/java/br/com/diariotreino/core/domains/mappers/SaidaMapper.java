package br.com.diariotreino.core.domains.mappers;

import java.util.List;

/**
 * Mapper somente de saída: para recursos que a API só devolve.
 *
 * @param <E>   entidade JPA
 * @param <OUT> DTO de saída
 */
public interface SaidaMapper<E, OUT> {

    OUT toDto(E entidade);

    List<OUT> toDtoList(List<E> entidades);
}
