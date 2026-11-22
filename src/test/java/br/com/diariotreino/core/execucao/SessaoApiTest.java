package br.com.diariotreino.core.execucao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.diariotreino.ApiTestBase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

class SessaoApiTest extends ApiTestBase {

    private static final int SUPINO_RETO = 1;

    private long criarTreino(String token) throws Exception {
        var corpo = Map.of("letra", "A", "nome", "Peito e Tríceps",
                "exercicios", List.of(Map.of("exercicioId", SUPINO_RETO, "seriesPlanejadas", 4, "repeticoesPlanejadas", 10)));
        String resposta = mvc.perform(comCorpo(comToken(post("/api/treinos"), token), corpo))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return lerJson(resposta).get("id").asLong();
    }

    private Map<String, Object> sessao(long treinoId, Instant inicio, Instant fim, List<?> series) {
        return Map.of("treinoId", treinoId, "inicio", inicio.toString(), "fim", fim.toString(), "series", series);
    }

    private List<Map<String, Object>> duasSeriesDeSupino() {
        return List.of(
                Map.of("exercicioId", SUPINO_RETO, "numero", 1, "cargaKg", 60, "repeticoes", 10, "concluida", true),
                Map.of("exercicioId", SUPINO_RETO, "numero", 2, "cargaKg", 62.5, "repeticoes", 8, "concluida", true));
    }

    @Test
    @DisplayName("A sessão registrada aparece no histórico e atualiza a última carga do exercício no treino")
    void deveRegistrarSessaoEAtualizarUltimaCargaQuandoSeriesForemValidas() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long treinoId = criarTreino(token);
        Instant fim = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/sessoes"), token),
                sessao(treinoId, fim.minus(58, ChronoUnit.MINUTES), fim, duasSeriesDeSupino())));

        // then
        resultado.andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTreino").value("Peito e Tríceps"))
                .andExpect(jsonPath("$.letraTreino").value("A"))
                .andExpect(jsonPath("$.series.length()").value(2));
        mvc.perform(comToken(get("/api/sessoes"), token))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].series[1].cargaKg").value(62.5));
        mvc.perform(comToken(get("/api/treinos/" + treinoId), token))
                .andExpect(jsonPath("$.exercicios[0].ultimaCargaKg").value(62.5));
    }

    @Test
    @DisplayName("Não é possível registrar sessão no treino de outro usuário")
    void deveResponder404QuandoTreinoDaSessaoForDeOutroUsuario() throws Exception {
        // given
        long treinoDeOutro = criarTreino(tokenNovoUsuario());
        Instant fim = Instant.now();

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/sessoes"), tokenNovoUsuario()),
                sessao(treinoDeOutro, fim.minus(50, ChronoUnit.MINUTES), fim, duasSeriesDeSupino())));

        // then
        resultado.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("A sessão com fim antes do início é recusada")
    void deveRecusarSessaoQuandoFimForAntesDoInicio() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long treinoId = criarTreino(token);
        Instant inicio = Instant.now().minus(1, ChronoUnit.HOURS);

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/sessoes"), token),
                sessao(treinoId, inicio, inicio.minus(5, ChronoUnit.MINUTES), duasSeriesDeSupino())));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O fim do treino não pode ser antes do início."));
    }

    @Test
    @DisplayName("A sessão sem nenhuma série é recusada com a mensagem no campo series")
    void deveRecusarSessaoQuandoNaoTiverSeries() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long treinoId = criarTreino(token);
        Instant fim = Instant.now();

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/sessoes"), token),
                sessao(treinoId, fim.minus(30, ChronoUnit.MINUTES), fim, List.of())));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.series").value("Conclua pelo menos uma série para salvar o treino."));
    }

    @Test
    @DisplayName("A mesma série enviada duas vezes é recusada")
    void deveRecusarSessaoQuandoSerieForRepetida() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long treinoId = criarTreino(token);
        Instant fim = Instant.now();
        var serie = Map.of("exercicioId", SUPINO_RETO, "numero", 1, "cargaKg", 60, "repeticoes", 10);

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/sessoes"), token),
                sessao(treinoId, fim.minus(30, ChronoUnit.MINUTES), fim, List.of(serie, serie))));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("A série 1 foi enviada mais de uma vez."));
    }
}
