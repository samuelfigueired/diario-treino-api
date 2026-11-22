package br.com.diariotreino.core.exception;

/** Recurso inexistente ou de outro usuário (404). */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String recurso, Object id) {
        super(recurso + " não encontrado: " + id);
    }
}
