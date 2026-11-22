package br.com.diariotreino.core.treino.services.impl;

import br.com.diariotreino.core.domains.mappers.BaseMapper;
import br.com.diariotreino.core.entidades.Exercicio;
import br.com.diariotreino.core.entidades.SerieRegistrada;
import br.com.diariotreino.core.entidades.Treino;
import br.com.diariotreino.core.entidades.TreinoExercicio;
import br.com.diariotreino.core.exception.BadRequestException;
import br.com.diariotreino.core.exception.NotFoundException;
import br.com.diariotreino.core.execucao.repositorys.SerieRegistradaRepository;
import br.com.diariotreino.core.exercicio.repositorys.ExercicioRepository;
import br.com.diariotreino.core.repositorys.RepositorioDoUsuario;
import br.com.diariotreino.core.services.impl.CrudServicePadrao;
import br.com.diariotreino.core.treino.domains.dtos.TreinoExercicioInDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoInDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoOutDTO;
import br.com.diariotreino.core.treino.domains.mappers.TreinoMapper;
import br.com.diariotreino.core.treino.repositorys.TreinoRepository;
import br.com.diariotreino.core.treino.services.TreinoService;
import br.com.diariotreino.core.util.CurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TreinoServicePadrao extends CrudServicePadrao<Treino, TreinoInDTO, TreinoOutDTO> implements TreinoService {

    private final TreinoRepository repository;
    private final TreinoMapper mapper;
    private final ExercicioRepository exercicioRepository;
    private final SerieRegistradaRepository serieRepository;

    @Override
    protected RepositorioDoUsuario<Treino> repository() {
        return repository;
    }

    @Override
    protected BaseMapper<Treino, TreinoInDTO, TreinoOutDTO> mapper() {
        return mapper;
    }

    @Override
    protected String recurso() {
        return "Treino";
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreinoOutDTO> listar() {
        return repository.findByUsuarioIdAndAtivoTrueOrderByLetraAsc(CurrentUser.usuarioId()).stream()
                .map(this::paraSaida)
                .toList();
    }

    @Override
    protected void validar(Treino treino, TreinoInDTO dto) {
        Long usuarioId = CurrentUser.usuarioId();
        treino.setLetra(treino.getLetra().trim().toUpperCase(Locale.ROOT));
        treino.setNome(treino.getNome().trim());
        Long id = treino.getId() == null ? -1L : treino.getId();
        if (repository.existsByUsuarioIdAndAtivoTrueAndLetraIgnoreCaseAndIdNot(usuarioId, treino.getLetra(), id)) {
            throw new BadRequestException("Você já tem um Treino " + treino.getLetra() + ". Escolha outra letra.");
        }
        if (treino.getDiasDaSemana() == null) treino.setDiasDaSemana(new LinkedHashSet<>());
        if (dto.getExercicios() != null) substituirExercicios(treino, dto.getExercicios(), usuarioId);
    }

    /** Recria a lista de exercícios na ordem enviada, aceitando só exercícios visíveis para o usuário. */
    private void substituirExercicios(Treino treino, List<TreinoExercicioInDTO> itens, Long usuarioId) {
        treino.getExercicios().clear();
        for (int i = 0; i < itens.size(); i++) {
            TreinoExercicioInDTO item = itens.get(i);
            Exercicio exercicio = exercicioRepository.visivel(item.getExercicioId(), usuarioId)
                    .orElseThrow(() -> new BadRequestException("Exercício não encontrado: " + item.getExercicioId()));
            TreinoExercicio te = new TreinoExercicio();
            te.setTreino(treino);
            te.setExercicio(exercicio);
            te.setOrdem(i + 1);
            te.setSeriesPlanejadas(item.getSeriesPlanejadas());
            te.setRepeticoesPlanejadas(item.getRepeticoesPlanejadas());
            te.setCargaPlanejadaKg(item.getCargaPlanejadaKg());
            treino.getExercicios().add(te);
        }
    }

    @Override
    protected TreinoOutDTO paraSaida(Treino treino) {
        Long usuarioId = treino.getUsuarioId();
        Map<Long, BigDecimal> ultimas = new HashMap<>();
        for (TreinoExercicio item : treino.getExercicios()) {
            Long exercicioId = item.getExercicio().getId();
            serieRepository.ultimaSerie(usuarioId, exercicioId)
                    .map(SerieRegistrada::getCargaKg)
                    .ifPresent(carga -> ultimas.put(exercicioId, carga));
        }
        return mapper.toDto(treino, ultimas);
    }

    /** Treino excluído não aparece mais, mas continua existindo para o histórico. */
    @Override
    public Treino buscarEntidade(Long id) {
        Treino treino = super.buscarEntidade(id);
        if (!Boolean.TRUE.equals(treino.getAtivo())) throw new NotFoundException(recurso(), id);
        return treino;
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        buscarEntidade(id).setAtivo(false);
    }
}
