package br.com.diariotreino.core.exercicio.repositorys;

import br.com.diariotreino.core.entidades.Exercicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/** Exercícios do catálogo (usuarioId nulo) e personalizados. Sempre consulte pelos métodos "visíveis". */
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    @Query("""
           select e from Exercicio e
           where e.usuarioId is null or e.usuarioId = :usuarioId
           order by e.grupoMuscular, e.nome
           """)
    List<Exercicio> visiveis(@Param("usuarioId") Long usuarioId);

    @Query("select e from Exercicio e where e.id = :id and (e.usuarioId is null or e.usuarioId = :usuarioId)")
    Optional<Exercicio> visivel(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    long countByUsuarioIdIsNull();
}
