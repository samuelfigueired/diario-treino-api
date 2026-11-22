package br.com.diariotreino.core.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/** Exercício dentro de um treino, com a ordem e o planejado (séries × repetições). */
@Entity
@Table(name = "treino_exercicios")
@Getter
@Setter
public class TreinoExercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "treino_id")
    private Treino treino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercicio_id")
    private Exercicio exercicio;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Integer seriesPlanejadas;

    @Column(nullable = false)
    private Integer repeticoesPlanejadas;

    /** Carga alvo definida pelo usuário (kg). Nula = sem alvo; o app usa a última carga registrada. */
    @Column(precision = 7, scale = 2)
    private BigDecimal cargaPlanejadaKg;
}
