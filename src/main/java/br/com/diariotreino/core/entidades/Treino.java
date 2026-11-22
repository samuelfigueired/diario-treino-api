package br.com.diariotreino.core.entidades;

import br.com.diariotreino.core.entidades.enums.DiaSemana;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Treino da divisão do usuário (Treino A · Peito e Tríceps). Exclusão é lógica: o histórico guarda o treinoId. */
@Entity
@Table(name = "treinos")
@Getter
@Setter
public class Treino extends EntidadeDoUsuario {

    @Column(nullable = false, length = 3)
    private String letra;

    @Column(nullable = false, length = 80)
    private String nome;

    @ElementCollection
    @CollectionTable(name = "treino_dias", joinColumns = @JoinColumn(name = "treino_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia", length = 3)
    private Set<DiaSemana> diasDaSemana = new LinkedHashSet<>();

    private Integer duracaoEstimadaMin;

    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem")
    private List<TreinoExercicio> exercicios = new ArrayList<>();

    private Boolean ativo = true;
}
