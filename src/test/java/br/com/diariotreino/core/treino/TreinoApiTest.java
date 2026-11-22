package br.com.diariotreino.core.treino;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.diariotreino.ApiTestBase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class TreinoApiTest extends ApiTestBase {

    private Map<String, Object> treinoPeito(String letra) {
        return Map.of(
                "letra", letra,
                "nome", "Peito e Tríceps",
                "diasDaSemana", List.of("SEG", "QUI"),
                "duracaoEstimadaMin", 55,
                "exercicios", List.of(
                        Map.of("exercicioId", 1, "seriesPlanejadas", 4, "repeticoesPlanejadas", 10, "cargaPlanejadaKg", 47.5),
                        Map.of("exercicioId", 4, "seriesPlanejadas", 3, "repeticoesPlanejadas", 12)));
    }

    private long criarTreino(String token, String letra) throws Exception {
        String resposta = mvc.perform(comCorpo(comToken(post("/api/treinos"), token), treinoPeito(letra)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return lerJson(resposta).get("id").asLong();
    }

    @Test
    @DisplayName("A usuária de demonstração vê os treinos A a D com a última carga de cada exercício")
    void deveListarTreinosComUltimaCargaQuandoUsuarioTemHistorico() throws Exception {
        // given
        String token = tokenDemo();

        // when
        var resultado = mvc.perform(comToken(get("/api/treinos"), token));

        // then
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].letra").value(org.hamcrest.Matchers.contains("A", "B", "C", "D")))
                .andExpect(jsonPath("$[0].exercicios[0].exercicio.nome").value("Supino reto com barra"))
                .andExpect(jsonPath("$[0].exercicios[0].ultimaCargaKg").isNumber());
    }

    @Test
    @DisplayName("O treino criado guarda os exercícios na ordem enviada, sem carga anterior")
    void deveCriarTreinoComExerciciosNaOrdemQuandoDadosForemValidos() throws Exception {
        // given
        String token = tokenNovoUsuario();

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/treinos"), token), treinoPeito("a")));

        // then
        resultado.andExpect(status().isCreated())
                .andExpect(jsonPath("$.letra").value("A"))
                .andExpect(jsonPath("$.exercicios[0].ordem").value(1))
                .andExpect(jsonPath("$.exercicios[0].cargaPlanejadaKg").value(47.5))
                .andExpect(jsonPath("$.exercicios[1].exercicio.nome").value("Tríceps na polia com corda"))
                .andExpect(jsonPath("$.exercicios[0].ultimaCargaKg").value(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    @DisplayName("Não é possível ter dois treinos ativos com a mesma letra")
    void deveRecusarTreinoQuandoLetraJaEstiverEmUso() throws Exception {
        // given
        String token = tokenNovoUsuario();
        criarTreino(token, "B");

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/treinos"), token), treinoPeito("B")));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Você já tem um Treino B. Escolha outra letra."));
    }

    @Test
    @DisplayName("Um usuário não consegue ver nem excluir o treino de outro usuário (responde 404)")
    void deveResponder404QuandoTreinoForDeOutroUsuario() throws Exception {
        // given
        long treinoDoLucas = criarTreino(tokenNovoUsuario(), "A");
        String outroUsuario = tokenNovoUsuario();

        // when
        var busca = mvc.perform(comToken(get("/api/treinos/" + treinoDoLucas), outroUsuario));
        var exclusao = mvc.perform(comToken(delete("/api/treinos/" + treinoDoLucas), outroUsuario));

        // then
        busca.andExpect(status().isNotFound());
        exclusao.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("O treino excluído some da lista e não pode mais ser aberto")
    void deveEsconderTreinoQuandoForExcluido() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long id = criarTreino(token, "C");

        // when
        mvc.perform(comToken(delete("/api/treinos/" + id), token)).andExpect(status().isNoContent());

        // then
        String lista = mvc.perform(comToken(get("/api/treinos"), token)).andReturn().getResponse().getContentAsString();
        assertThat(lerJson(lista)).isEmpty();
        mvc.perform(comToken(get("/api/treinos/" + id), token)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Treino com exercício inexistente é recusado")
    void deveRecusarTreinoQuandoExercicioNaoExistir() throws Exception {
        // given
        var corpo = Map.of("letra", "E", "nome", "Braços",
                "exercicios", List.of(Map.of("exercicioId", 99999, "seriesPlanejadas", 3, "repeticoesPlanejadas", 12)));

        // when
        var resultado = mvc.perform(comCorpo(comToken(post("/api/treinos"), tokenNovoUsuario()), corpo));

        // then
        resultado.andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value("Exercício não encontrado: 99999"));
    }

    @Test
    @DisplayName("A edição do treino troca nome, dias e exercícios, mantendo o mesmo id")
    void deveAtualizarTreinoQuandoDadosForemValidos() throws Exception {
        // given
        String token = tokenNovoUsuario();
        long id = criarTreino(token, "A");
        var edicao = Map.of("letra", "A", "nome", "Peito e Ombros", "diasDaSemana", List.of("TER"),
                "exercicios", List.of(Map.of("exercicioId", 16, "seriesPlanejadas", 4, "repeticoesPlanejadas", 8, "cargaPlanejadaKg", 18)));

        // when
        var resultado = mvc.perform(comCorpo(comToken(put("/api/treinos/" + id), token), edicao));

        // then
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Peito e Ombros"))
                .andExpect(jsonPath("$.diasDaSemana[0]").value("TER"))
                .andExpect(jsonPath("$.exercicios.length()").value(1))
                .andExpect(jsonPath("$.exercicios[0].exercicio.nome").value("Desenvolvimento com halteres"));
    }
}
