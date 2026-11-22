package br.com.diariotreino.comum.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Base das entidades que pertencem a um usuário (treino, sessão, série...).
 * O usuarioId vem sempre do usuário logado, nunca do corpo da requisição.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class EntidadeDoUsuario extends EntidadeBase {

    @Column(nullable = false, updatable = false)
    private Long usuarioId;
}
