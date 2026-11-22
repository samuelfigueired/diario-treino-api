package br.com.diariotreino.core.treino.repositorys;

import br.com.diariotreino.core.entidades.Treino;
import br.com.diariotreino.core.repositorys.RepositorioDoUsuario;

import java.util.List;

public interface TreinoRepository extends RepositorioDoUsuario<Treino> {

    List<Treino> findByUsuarioIdAndAtivoTrueOrderByLetraAsc(Long usuarioId);

    boolean existsByUsuarioIdAndAtivoTrueAndLetraIgnoreCaseAndIdNot(Long usuarioId, String letra, Long id);
}
