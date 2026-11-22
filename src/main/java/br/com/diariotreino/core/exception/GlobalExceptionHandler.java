package br.com.diariotreino.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/** Converte as exceções em ProblemDetail com mensagem para o usuário. */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MENSAGEM_DADOS_INVALIDOS = "Confira os campos destacados.";
    private static final String MENSAGEM_CORPO_INVALIDO = "Os dados enviados estão em formato inválido.";
    private static final String MENSAGEM_ERRO_INTERNO = "Não foi possível concluir agora. Tente novamente em instantes.";

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail tratarBadRequest(BadRequestException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail tratarNotFound(NotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MuitasTentativasException.class)
    public ProblemDetail tratarMuitasTentativas(MuitasTentativasException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, MENSAGEM_DADOS_INVALIDOS);
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        problema.setProperty("campos", campos);
        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail tratarCorpoInvalido(HttpMessageNotReadableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, MENSAGEM_CORPO_INVALIDO);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail tratarRotaInexistente(NoResourceFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Rota não encontrada.");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarInesperado(Exception e) {
        log.error("Erro inesperado ao processar a requisição", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, MENSAGEM_ERRO_INTERNO);
    }
}
