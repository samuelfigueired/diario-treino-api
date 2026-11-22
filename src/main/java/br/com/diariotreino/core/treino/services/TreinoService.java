package br.com.diariotreino.core.treino.services;

import br.com.diariotreino.core.services.CrudService;
import br.com.diariotreino.core.treino.domains.dtos.TreinoInDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoOutDTO;

import java.util.List;

public interface TreinoService extends CrudService<TreinoInDTO, TreinoOutDTO> {

    /** Treinos ativos do usuário logado, em ordem de letra. */
    List<TreinoOutDTO> listar();
}
