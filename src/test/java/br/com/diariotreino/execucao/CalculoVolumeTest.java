package br.com.diariotreino.execucao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculoVolumeTest {

    @Test
    @DisplayName("O volume da série é a carga multiplicada pelas repetições")
    void deveCalcularVolumeQuandoSerieTemCargaERepeticoes() {
        // given
        var carga = new BigDecimal("62.50");
        var repeticoes = 10;

        // when
        var volume = CalculoVolume.daSerie(carga, repeticoes);

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

    @Test
    @DisplayName("Série sem repetições registradas tem volume zero")
    void deveRetornarZeroQuandoRepeticoesForemZero() {
        // given
        var carga = new BigDecimal("40.00");

        // when
        var volume = CalculoVolume.daSerie(carga, 0);

        // then
        assertThat(volume).isEqualByComparingTo("0.00");
    }
}
