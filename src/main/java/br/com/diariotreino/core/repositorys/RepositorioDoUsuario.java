package br.com.diariotreino.core.repositorys;

import br.com.diariotreino.core.entidades.EntidadeDoUsuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/** Repositório base de entidades do usuário: toda busca por id já filtra pelo dono. */
@NoRepositoryBean
public interface RepositorioDoUsuario<E extends EntidadeDoUsuario> extends JpaRepository<E, Long> {

    Optional<E> findByIdAndUsuarioId(Long id, Long usuarioId);
}
