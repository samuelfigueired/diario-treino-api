package br.com.diariotreino.core.auth.domains.dtos;

import br.com.diariotreino.core.usuario.domains.dtos.UsuarioOutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutDTO {

    private String token;

    private UsuarioOutDTO usuario;
}
