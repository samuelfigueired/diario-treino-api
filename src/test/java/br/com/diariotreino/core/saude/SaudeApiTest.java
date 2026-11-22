package br.com.diariotreino.core.saude;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.diariotreino.ApiTestBase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SaudeApiTest extends ApiTestBase {

    @Test
    @DisplayName("A verificação de saúde responde sem login")
    void deveResponderOkQuandoChamadaSemLogin() throws Exception {
        // given
        var rota = "/api/saude";

        // when
        var resultado = mvc.perform(get(rota));

        // then
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    @DisplayName("Rotas da API fora da lista pública exigem login e respondem 401 sem token")
    void deveExigirLoginQuandoRotaNaoForPublica() throws Exception {
        // given
        var rota = "/api/treinos";

        // when
        var resultado = mvc.perform(get(rota));

        // then
        resultado.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Token inválido é tratado como requisição sem login")
    void deveResponder401QuandoTokenForInvalido() throws Exception {
        // given
        var tokenFalso = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.assinatura-invalida";

        // when
        var resultado = mvc.perform(comToken(get("/api/treinos"), tokenFalso));

        // then
        resultado.andExpect(status().isUnauthorized());
    }
}
