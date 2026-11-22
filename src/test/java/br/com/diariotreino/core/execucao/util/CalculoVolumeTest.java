package br.com.diariotreino.core.execucao.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CalculoVolumeTest {

    @Test
    @DisplayName("O volume da série é a carga multiplicada pelas repetições")
    void deveCalcularVolumeQuandoSerieTemCargaERepeticoes() {
        // given
        var carga = new BigDecimal("62.50");

        // when
        var volume = CalculoVolume.daSerie(carga, 10);

        // then
        assertThat(volume).isEqualByComparingTo("625.00");
    }

    @Test
    @DisplayName("Série com peso corporal (sem carga) tem volume zero")
    void deveRetornarZeroQuandoCargaForNula() {
        // given
        BigDecimal carga = null;

        // when
        var volume = CalculoVolume.daSerie(carga, 12);

        // then
        assertThat(volume).isEqualByComparingTo("0.00");
    }
}
