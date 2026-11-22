package br.com.diariotreino.core.entidades.enums;

import java.time.DayOfWeek;

/** Dia da semana planejado para um treino (gravado como texto). */
public enum DiaSemana {
    SEG, TER, QUA, QUI, SEX, SAB, DOM;

    public static DiaSemana de(DayOfWeek dia) {
        return values()[dia.getValue() - 1];
    }
}
