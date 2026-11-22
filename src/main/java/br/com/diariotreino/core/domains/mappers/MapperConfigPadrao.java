package br.com.diariotreino.core.domains.mappers;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Configuração compartilhada por todos os mappers: beans do Spring, campos sem correspondência são ignorados
 * (id, usuarioId e criadoEm nunca vêm do app) e, na atualização, valores nulos não apagam o que já existe.
 */
@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperConfigPadrao {
}
