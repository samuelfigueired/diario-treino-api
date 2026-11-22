package br.com.diariotreino.core.execucao.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Regras puras de volume de treino (carga × repetições). */
public final class CalculoVolume {

    private CalculoVolume() {
    }

    /** Volume de uma série em kg. Carga nula conta como peso corporal (volume zero). */
    public static BigDecimal daSerie(BigDecimal cargaKg, Integer repeticoes) {
        if (cargaKg == null || repeticoes == null || repeticoes <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return cargaKg.multiply(BigDecimal.valueOf(repeticoes)).setScale(2, RoundingMode.HALF_UP);
    }
}
