package br.com.diariotreino.core.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Dados do usuário logado, extraídos do JWT. */
@Getter
@AllArgsConstructor
public class UsuarioPrincipal {

    private Long id;

    private String nome;

    private String email;
}
