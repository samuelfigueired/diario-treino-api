package br.com.diariotreino.core.entidades;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Treino realizado. Guarda nome e letra do treino no momento da execução,
 * para o histórico continuar certo se o treino for renomeado ou excluído.
 */
@Entity
@Table(name = "sessoes_treino")
@Getter
@Setter
public class SessaoTreino extends EntidadeDoUsuario {

    private Long treinoId;

    @Column(nullable = false, length = 80)
    private String nomeTreino;

    @Column(nullable = false, length = 3)
    private String letraTreino;

    @Column(nullable = false)
    private Instant inicio;

    private Instant fim;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("exercicioId, numero")
    private List<SerieRegistrada> series = new ArrayList<>();
}
