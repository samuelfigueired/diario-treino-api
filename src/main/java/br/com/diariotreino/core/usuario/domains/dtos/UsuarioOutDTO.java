package br.com.diariotreino.core.usuario.domains.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Usuário devolvido pela API (o hash da senha nunca sai aqui). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioOutDTO {

    private Long id;

    private String nome;

    private String email;
}
