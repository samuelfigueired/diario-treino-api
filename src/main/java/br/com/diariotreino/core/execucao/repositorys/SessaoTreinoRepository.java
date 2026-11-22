package br.com.diariotreino.core.execucao.repositorys;

import br.com.diariotreino.core.entidades.SessaoTreino;
import br.com.diariotreino.core.repositorys.RepositorioDoUsuario;

import org.springframework.data.jpa.repository.EntityGraph;

import java.time.Instant;
import java.util.List;

public interface SessaoTreinoRepository extends RepositorioDoUsuario<SessaoTreino> {

    /** Sessões a partir de uma data, mais recentes primeiro, já com as séries (evita N+1). */
    @EntityGraph(attributePaths = "series")
    List<SessaoTreino> findByUsuarioIdAndInicioGreaterThanEqualOrderByInicioDesc(Long usuarioId, Instant desde);
}
