package br.com.diariotreino.comum.excecao;

/** Recurso inexistente ou de outro usuário (404). */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensagem) {
        super(mensagem);
    }
}
