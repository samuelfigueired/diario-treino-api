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

/** Série feita numa sessão. Carga nula = peso corporal. */
@Entity
@Table(name = "series_registradas")
@Getter
@Setter
public class SerieRegistrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sessao_id")
    private SessaoTreino sessao;

    @Column(nullable = false)
    private Long exercicioId;

    @Column(nullable = false)
    private Integer numero;

    @Column(precision = 7, scale = 2)
    private BigDecimal cargaKg;

    private Integer repeticoes;

    private Boolean concluida = true;
}
