package br.com.diariotreino.core.services;

/** Operações básicas de cadastro, sempre restritas ao usuário logado. */
public interface CrudService<IN, OUT> {

    OUT buscar(Long id);

    OUT criar(IN dto);

    OUT atualizar(Long id, IN dto);

    void excluir(Long id);
}
