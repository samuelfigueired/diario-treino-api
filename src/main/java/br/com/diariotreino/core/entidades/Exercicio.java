package br.com.diariotreino.core.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Exercício. Sem usuarioId é do catálogo padrão (visível para todos);
 * com usuarioId é personalizado e só o dono vê.
 */
@Entity
@Table(name = "exercicios")
@Getter
@Setter
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 60)
    private String grupoMuscular;

    @Column(name = "usuario_id", updatable = false)
    private Long usuarioId;
}
