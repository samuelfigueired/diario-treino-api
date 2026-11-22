package br.com.diariotreino.core.auth.services;

import br.com.diariotreino.core.auth.domains.dtos.CadastroInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginOutDTO;
import br.com.diariotreino.core.usuario.domains.dtos.UsuarioOutDTO;

public interface AuthService {

    LoginOutDTO login(LoginInDTO dto);

    LoginOutDTO cadastrar(CadastroInDTO dto);

    UsuarioOutDTO me();
}
