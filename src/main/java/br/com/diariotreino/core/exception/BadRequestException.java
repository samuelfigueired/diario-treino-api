package br.com.diariotreino.core.exception;

/** Regra de negócio violada (400). A mensagem vai para o usuário: curta e em português. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensagem) {
        super(mensagem);
    }
}
