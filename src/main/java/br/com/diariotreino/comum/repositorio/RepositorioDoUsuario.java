package br.com.diariotreino.comum.repositorio;

import br.com.diariotreino.comum.entidade.EntidadeDoUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositório de entidades do usuário. Use sempre os métodos que recebem o usuarioId;
 * nunca findById para dados de usuário.
 */
@NoRepositoryBean
public interface RepositorioDoUsuario<T extends EntidadeDoUsuario> extends JpaRepository<T, Long> {

    Optional<T> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<T> findAllByUsuarioId(Long usuarioId);
}
