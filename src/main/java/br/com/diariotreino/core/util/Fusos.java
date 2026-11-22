package br.com.diariotreino.core.util;

import java.time.LocalDate;
import java.time.ZoneId;

/** Fuso da operação. Use sempre esta constante para calcular "hoje" e a semana corrente. */
public final class Fusos {

    public static final ZoneId SAO_PAULO = ZoneId.of("America/Sao_Paulo");

    private Fusos() {
    }

    public static LocalDate hoje() {
        return LocalDate.now(SAO_PAULO);
    }
}
