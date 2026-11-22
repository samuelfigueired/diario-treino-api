package br.com.diariotreino.core.execucao.services.impl;

import br.com.diariotreino.core.entidades.SessaoTreino;
import br.com.diariotreino.core.entidades.Treino;
import br.com.diariotreino.core.exception.BadRequestException;
import br.com.diariotreino.core.exception.NotFoundException;
import br.com.diariotreino.core.execucao.domains.dtos.SerieInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoOutDTO;
import br.com.diariotreino.core.execucao.domains.mappers.SessaoMapper;
import br.com.diariotreino.core.execucao.repositorys.SessaoTreinoRepository;
import br.com.diariotreino.core.execucao.services.SessaoService;
import br.com.diariotreino.core.exercicio.repositorys.ExercicioRepository;
import br.com.diariotreino.core.treino.repositorys.TreinoRepository;
import br.com.diariotreino.core.util.CurrentUser;
import br.com.diariotreino.core.util.Fusos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoServicePadrao implements SessaoService {

    private static final String RECURSO = "Sessão";
    private static final Duration TOLERANCIA_RELOGIO = Duration.ofMinutes(10);
    private static final Duration DURACAO_MAXIMA = Duration.ofHours(6);

    private final SessaoTreinoRepository repository;
    private final SessaoMapper mapper;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SessaoOutDTO> listar(LocalDate desde) {
        LocalDate inicio = desde != null ? desde : Fusos.hoje().minusYears(1);
        Instant limite = inicio.atStartOfDay(Fusos.SAO_PAULO).toInstant();
        return mapper.toDtoList(repository.findByUsuarioIdAndInicioGreaterThanEqualOrderByInicioDesc(CurrentUser.usuarioId(), limite));
    }

    @Override
    @Transactional(readOnly = true)
    public SessaoOutDTO buscar(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Override
    @Transactional
    public SessaoOutDTO registrar(SessaoInDTO dto) {
        Long usuarioId = CurrentUser.usuarioId();
        Treino treino = treinoRepository.findByIdAndUsuarioId(dto.getTreinoId(), usuarioId)
                .orElseThrow(() -> new NotFoundException("Treino", dto.getTreinoId()));
        validarHorarios(dto.getInicio(), dto.getFim());
        validarSeries(dto.getSeries(), usuarioId);

        SessaoTreino sessao = mapper.toEntidade(dto);
        sessao.setNomeTreino(treino.getNome());
        sessao.setLetraTreino(treino.getLetra());
        sessao.getSeries().forEach(serie -> serie.setSessao(sessao));
        SessaoTreino salva = repository.save(sessao);
        log.info("Sessão registrada: usuario {}, sessao {}, treino {}", usuarioId, salva.getId(), treino.getId());
        return mapper.toDto(salva);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private SessaoTreino buscarEntidade(Long id) {
        return repository.findByIdAndUsuarioId(id, CurrentUser.usuarioId())
                .orElseThrow(() -> new NotFoundException(RECURSO, id));
    }

    static void validarHorarios(Instant inicio, Instant fim) {
        if (fim.isBefore(inicio)) throw new BadRequestException("O fim do treino não pode ser antes do início.");
        if (inicio.isAfter(Instant.now().plus(TOLERANCIA_RELOGIO))) {
            throw new BadRequestException("O início do treino está no futuro. Confira a hora do celular.");
        }
        if (Duration.between(inicio, fim).compareTo(DURACAO_MAXIMA) > 0) {
            throw new BadRequestException("O treino passou de 6 horas. Confira os horários.");
        }
    }

    private void validarSeries(List<SerieInDTO> series, Long usuarioId) {
        Set<String> vistas = new HashSet<>();
        Set<Long> exerciciosConferidos = new HashSet<>();
        for (SerieInDTO serie : series) {
            if (!vistas.add(serie.getExercicioId() + ":" + serie.getNumero())) {
                throw new BadRequestException("A série " + serie.getNumero() + " foi enviada mais de uma vez.");
            }
            if (exerciciosConferidos.add(serie.getExercicioId())
                    && exercicioRepository.visivel(serie.getExercicioId(), usuarioId).isEmpty()) {
                throw new BadRequestException("Exercício não encontrado: " + serie.getExercicioId());
            }
        }
    }
}
