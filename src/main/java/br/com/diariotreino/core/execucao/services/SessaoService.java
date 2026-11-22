package br.com.diariotreino.core.execucao.services;

import br.com.diariotreino.core.execucao.domains.dtos.SessaoInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoOutDTO;

import java.time.LocalDate;
import java.util.List;

public interface SessaoService {

    /** Sessões do usuário a partir de `desde` (nulo = último ano), mais recentes primeiro. */
    List<SessaoOutDTO> listar(LocalDate desde);

    SessaoOutDTO buscar(Long id);

    /** RF04: grava o treino realizado com as séries, repetições e cargas. */
    SessaoOutDTO registrar(SessaoInDTO dto);

    /** Corrige horários, cargas e repetições de uma sessão já salva (o treino de origem não muda). */
    SessaoOutDTO atualizar(Long id, SessaoInDTO dto);

    void excluir(Long id);
}
