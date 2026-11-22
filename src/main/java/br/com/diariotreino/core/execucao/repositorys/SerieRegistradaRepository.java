package br.com.diariotreino.core.execucao.repositorys;

import br.com.diariotreino.core.entidades.SerieRegistrada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieRegistradaRepository extends JpaRepository<SerieRegistrada, Long> {

    /** Última série concluída do exercício pelo usuário (sessão mais recente, maior número de série). */
    Optional<SerieRegistrada> findFirstBySessaoUsuarioIdAndExercicioIdAndConcluidaTrueOrderBySessaoInicioDescNumeroDesc(
            Long usuarioId, Long exercicioId);

    default Optional<SerieRegistrada> ultimaSerie(Long usuarioId, Long exercicioId) {
        return findFirstBySessaoUsuarioIdAndExercicioIdAndConcluidaTrueOrderBySessaoInicioDescNumeroDesc(usuarioId, exercicioId);
    }
}
