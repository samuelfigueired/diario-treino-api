package br.com.diariotreino.saude;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SaudeApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("A verificação de saúde responde sem login")
    void deveResponderOkQuandoChamadaSemLogin() throws Exception {
        // given
        var rota = "/api/saude";

        // when
        var resultado = mvc.perform(get(rota));

        // then
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
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
}
