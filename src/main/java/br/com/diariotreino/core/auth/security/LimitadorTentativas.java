package br.com.diariotreino.core.auth.security;

import br.com.diariotreino.core.exception.MuitasTentativasException;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Limita tentativas de login com erro por chave (e-mail), em memória. */
@Component
public class LimitadorTentativas {

    static final int MAXIMO_FALHAS = 5;
    static final Duration JANELA = Duration.ofMinutes(15);
    private static final String MENSAGEM = "Muitas tentativas. Aguarde 15 minutos e tente de novo.";

    private final Map<String, Deque<Instant>> falhas = new ConcurrentHashMap<>();
    private final Clock relogio;

    public LimitadorTentativas() {
        this(Clock.systemUTC());
    }

    LimitadorTentativas(Clock relogio) {
        this.relogio = relogio;
    }

    /** Lança 429 se a chave já atingiu o limite de falhas na janela. */
    public void verificar(String chave) {
        Deque<Instant> lista = falhas.get(chave);
        if (lista == null) return;
        synchronized (lista) {
            limpar(lista);
            if (lista.size() >= MAXIMO_FALHAS) throw new MuitasTentativasException(MENSAGEM);
        }
    }

    public void registrarFalha(String chave) {
        Deque<Instant> lista = falhas.computeIfAbsent(chave, k -> new ArrayDeque<>());
        synchronized (lista) {
            limpar(lista);
            lista.addLast(relogio.instant());
        }
    }

    public void limpar(String chave) {
        falhas.remove(chave);
    }

    private void limpar(Deque<Instant> lista) {
        Instant limite = relogio.instant().minus(JANELA);
        while (!lista.isEmpty() && lista.peekFirst().isBefore(limite)) lista.pollFirst();
    }
}
