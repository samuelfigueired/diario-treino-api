package br.com.diariotreino.core.usuario.domains.mappers;

import br.com.diariotreino.core.domains.mappers.MapperConfigPadrao;
import br.com.diariotreino.core.domains.mappers.SaidaMapper;
import br.com.diariotreino.core.entidades.Usuario;
import br.com.diariotreino.core.usuario.domains.dtos.UsuarioOutDTO;

import org.mapstruct.Mapper;

/** Só saída: o usuário é criado pelo AuthService (a senha vira hash antes de salvar). */
@Mapper(config = MapperConfigPadrao.class)
public interface UsuarioMapper extends SaidaMapper<Usuario, UsuarioOutDTO> {
}
