package br.com.diariotreino.core.auth.security;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.diariotreino.core.exception.MuitasTentativasException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

class LimitadorTentativasTest {

    private static final String EMAIL = "mariana.rocha@exemplo.com.br";

    @Test
    @DisplayName("Depois de 5 senhas erradas em 15 minutos, o login do e-mail fica bloqueado")
    void deveBloquearQuandoAtingirCincoFalhasNaJanela() {
        // given
        var limitador = new LimitadorTentativas(Clock.fixed(Instant.parse("2026-11-22T15:00:00Z"), ZoneOffset.UTC));
        for (int i = 0; i < LimitadorTentativas.MAXIMO_FALHAS; i++) limitador.registrarFalha(EMAIL);

        // when / then
        assertThatThrownBy(() -> limitador.verificar(EMAIL)).isInstanceOf(MuitasTentativasException.class);
    }

    @Test
    @DisplayName("Falhas mais antigas que 15 minutos deixam de contar")
    void deveLiberarQuandoFalhasSairemDaJanela() {
        // given
        var inicio = Instant.parse("2026-11-22T15:00:00Z");
        var antes = new LimitadorTentativas(Clock.fixed(inicio, ZoneOffset.UTC));
        for (int i = 0; i < LimitadorTentativas.MAXIMO_FALHAS; i++) antes.registrarFalha(EMAIL);
        var depois = new LimitadorTentativas(Clock.fixed(inicio.plus(Duration.ofMinutes(16)), ZoneOffset.UTC));
        for (int i = 0; i < LimitadorTentativas.MAXIMO_FALHAS - 1; i++) depois.registrarFalha(EMAIL);

        // when / then
        assertThatCode(() -> depois.verificar(EMAIL)).doesNotThrowAnyException();
    }
}
