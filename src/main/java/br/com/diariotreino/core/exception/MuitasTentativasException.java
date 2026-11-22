package br.com.diariotreino.core.exception;

/** Limite de tentativas atingido (429). */
public class MuitasTentativasException extends RuntimeException {

    public MuitasTentativasException(String mensagem) {
        super(mensagem);
    }
}
