package br.com.diariotreino.core.entidades;

import br.com.diariotreino.core.util.CurrentUser;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Base de toda entidade que pertence a um usuário (treino, sessão...).
 * O usuarioId é preenchido a partir do usuário logado, nunca do corpo da requisição.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeDoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, updatable = false)
    private Long usuarioId;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    void prePersist() {
        if (criadoEm == null) criadoEm = Instant.now();
        if (usuarioId == null) usuarioId = CurrentUser.usuarioId();
    }
}
