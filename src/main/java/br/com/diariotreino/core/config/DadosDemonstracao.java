package br.com.diariotreino.core.config;

import br.com.diariotreino.core.entidades.Exercicio;
import br.com.diariotreino.core.entidades.SerieRegistrada;
import br.com.diariotreino.core.entidades.SessaoTreino;
import br.com.diariotreino.core.entidades.Treino;
import br.com.diariotreino.core.entidades.TreinoExercicio;
import br.com.diariotreino.core.entidades.Usuario;
import br.com.diariotreino.core.entidades.enums.DiaSemana;
import br.com.diariotreino.core.execucao.repositorys.SessaoTreinoRepository;
import br.com.diariotreino.core.exercicio.repositorys.ExercicioRepository;
import br.com.diariotreino.core.treino.repositorys.TreinoRepository;
import br.com.diariotreino.core.usuario.repositorys.UsuarioRepository;
import br.com.diariotreino.core.util.Fusos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Usuária de demonstração com treinos A–D e 12 semanas de histórico, para testar o app sem cadastrar nada.
 * Desligada por padrão (os dados do app são reais). Nunca roda no perfil prod.
 * <ul>
 *   <li>APP_DEMO=true: cria a usuária mariana.rocha@exemplo.com.br / treino2026 com os dados.</li>
 *   <li>APP_DEMO_EMAIL=email: preenche uma conta que já existe (e ainda não tem treinos) com os mesmos dados.</li>
 * </ul>
 */
@Slf4j
@Component
@Profile("!prod")
@Order(2)
@RequiredArgsConstructor
public class DadosDemonstracao implements ApplicationRunner {

    public static final String EMAIL_DEMO = "mariana.rocha@exemplo.com.br";
    public static final String SENHA_DEMO = "treino2026";
    private static final int SEMANAS_DE_HISTORICO = 12;
    private static final int SEMANAS_POR_AUMENTO = 3;

    /** Exercício, séries, repetições, carga inicial (kg, nula = peso corporal) e aumento a cada 3 semanas. */
    private record Item(String exercicio, int series, int reps, String cargaInicial, String aumento) {
    }

    private record Plano(String letra, String nome, Set<DiaSemana> dias, int minutos, List<Item> itens) {
    }

    private static final List<Plano> PLANOS = List.of(
            new Plano("A", "Peito e Tríceps", Set.of(DiaSemana.SEG, DiaSemana.QUI), 55, List.of(
                    new Item("Supino reto com barra", 4, 10, "45", "2.5"),
                    new Item("Supino inclinado com halteres", 3, 12, "16", "1"),
                    new Item("Crucifixo na máquina", 3, 12, "30", "2.5"),
                    new Item("Tríceps na polia com corda", 4, 12, "20", "1.25"),
                    new Item("Tríceps testa", 3, 10, "15", "1"))),
            new Plano("B", "Costas e Bíceps", Set.of(DiaSemana.TER, DiaSemana.SEX), 52, List.of(
                    new Item("Puxada frontal", 4, 10, "45", "2.5"),
                    new Item("Remada curvada", 4, 10, "40", "2.5"),
                    new Item("Remada baixa", 3, 12, "40", "2.5"),
                    new Item("Rosca direta com barra", 3, 10, "20", "1"),
                    new Item("Rosca martelo", 3, 12, "10", "1"))),
            new Plano("C", "Pernas", Set.of(DiaSemana.QUA), 64, List.of(
                    new Item("Agachamento livre", 4, 8, "60", "5"),
                    new Item("Leg press 45°", 4, 12, "140", "10"),
                    new Item("Cadeira extensora", 3, 12, "40", "2.5"),
                    new Item("Mesa flexora", 3, 12, "30", "2.5"),
                    new Item("Panturrilha em pé", 4, 15, "50", "2.5"))),
            new Plano("D", "Ombros e Core", Set.of(DiaSemana.SAB), 46, List.of(
                    new Item("Desenvolvimento com halteres", 4, 10, "14", "1"),
                    new Item("Elevação lateral", 3, 12, "6", "0.5"),
                    new Item("Prancha abdominal", 3, 45, null, null))));

    private final UsuarioRepository usuarioRepository;
    private final ExercicioRepository exercicioRepository;
    private final TreinoRepository treinoRepository;
    private final SessaoTreinoRepository sessaoRepository;
    private final PasswordEncoder encoder;

    @Value("${app.demo.ativo:false}")
    private boolean demoAtivo;

    @Value("${app.demo.email:}")
    private String emailParaPreencher;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (demoAtivo && !usuarioRepository.existsByEmailIgnoreCase(EMAIL_DEMO)) {
            Usuario mariana = new Usuario();
            mariana.setNome("Mariana Alves Rocha");
            mariana.setEmail(EMAIL_DEMO);
            mariana.setSenhaHash(encoder.encode(SENHA_DEMO));
            preencher(usuarioRepository.save(mariana).getId());
        }
        if (!emailParaPreencher.isBlank()) preencherContaExistente(emailParaPreencher.trim());
    }

    /** Só preenche conta sem treinos, para nunca misturar dados fictícios com registros reais. */
    private void preencherContaExistente(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);
        if (usuario == null) {
            log.warn("APP_DEMO_EMAIL informado, mas a conta não existe. Crie a conta pelo app e reinicie a API.");
            return;
        }
        if (!treinoRepository.findByUsuarioIdAndAtivoTrueOrderByLetraAsc(usuario.getId()).isEmpty()) {
            log.info("Conta {} já tem treinos: dados de demonstração não aplicados", usuario.getId());
            return;
        }
        preencher(usuario.getId());
    }

    private void preencher(Long usuarioId) {
        Map<String, Exercicio> catalogo = exercicioRepository.visiveis(usuarioId).stream()
                .collect(Collectors.toMap(Exercicio::getNome, Function.identity()));
        for (Plano plano : PLANOS) {
            Treino treino = criarTreino(plano, usuarioId, catalogo);
            criarHistorico(plano, treino, catalogo);
        }
        log.info("Dados de demonstração criados: usuario {}", usuarioId);
    }

    private Treino criarTreino(Plano plano, Long usuarioId, Map<String, Exercicio> catalogo) {
        Treino treino = new Treino();
        treino.setUsuarioId(usuarioId);
        treino.setLetra(plano.letra());
        treino.setNome(plano.nome());
        treino.getDiasDaSemana().addAll(plano.dias());
        treino.setDuracaoEstimadaMin(plano.minutos());
        for (int i = 0; i < plano.itens().size(); i++) {
            Item item = plano.itens().get(i);
            TreinoExercicio te = new TreinoExercicio();
            te.setTreino(treino);
            te.setExercicio(catalogo.get(item.exercicio()));
            te.setOrdem(i + 1);
            te.setSeriesPlanejadas(item.series());
            te.setRepeticoesPlanejadas(item.reps());
            treino.getExercicios().add(te);
        }
        return treinoRepository.save(treino);
    }

    /** Uma sessão por dia planejado nas últimas semanas, pulando alguns dias para parecer uma rotina real. */
    private void criarHistorico(Plano plano, Treino treino, Map<String, Exercicio> catalogo) {
        LocalDate hoje = Fusos.hoje();
        for (int diasAtras = SEMANAS_DE_HISTORICO * 7; diasAtras >= 1; diasAtras--) {
            LocalDate dia = hoje.minusDays(diasAtras);
            if (!plano.dias().contains(DiaSemana.de(dia.getDayOfWeek())) || diasAtras % 9 == 4) continue;
            int semana = SEMANAS_DE_HISTORICO - diasAtras / 7;
            ZonedDateTime inicio = dia.atTime(7, 10 + (diasAtras % 4) * 5).atZone(Fusos.SAO_PAULO);

            SessaoTreino sessao = new SessaoTreino();
            sessao.setUsuarioId(treino.getUsuarioId());
            sessao.setTreinoId(treino.getId());
            sessao.setNomeTreino(treino.getNome());
            sessao.setLetraTreino(treino.getLetra());
            sessao.setInicio(inicio.toInstant());
            sessao.setFim(inicio.plusMinutes(plano.minutos() + (diasAtras % 5 - 2) * 3L).toInstant());
            for (Item item : plano.itens()) {
                for (int n = 1; n <= item.series(); n++) {
                    SerieRegistrada serie = new SerieRegistrada();
                    serie.setSessao(sessao);
                    serie.setExercicioId(catalogo.get(item.exercicio()).getId());
                    serie.setNumero(n);
                    serie.setCargaKg(carga(item, semana));
                    serie.setRepeticoes(item.reps() - (n == item.series() && diasAtras % 3 == 0 ? 2 : 0));
                    serie.setConcluida(true);
                    sessao.getSeries().add(serie);
                }
            }
            sessaoRepository.save(sessao);
        }
    }

    private static BigDecimal carga(Item item, int semana) {
        if (item.cargaInicial() == null) return null;
        int aumentos = semana / SEMANAS_POR_AUMENTO;
        return new BigDecimal(item.cargaInicial())
                .add(new BigDecimal(item.aumento()).multiply(BigDecimal.valueOf(aumentos)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
