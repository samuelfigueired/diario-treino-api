package br.com.diariotreino;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.diariotreino.core.config.DadosDemonstracao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

/** Base dos testes pela API: login da usuária de demonstração e criação de usuários novos (e-mails únicos). */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ApiTestBase {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper json;

    protected String tokenDemo() throws Exception {
        return token(post("/api/auth/login"), Map.of("email", DadosDemonstracao.EMAIL_DEMO, "senha", DadosDemonstracao.SENHA_DEMO), 200);
    }

    /** Cria uma conta nova e devolve o token (cada teste usa o seu usuário para não interferir nos outros). */
    protected String tokenNovoUsuario() throws Exception {
        String email = "lucas.pereira." + System.nanoTime() + "@exemplo.com.br";
        return token(post("/api/auth/cadastro"), Map.of("nome", "Lucas Pereira Souza", "email", email, "senha", "supino2026"), 201);
    }

    protected MockHttpServletRequestBuilder comToken(MockHttpServletRequestBuilder req, String token) {
        return req.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    protected MockHttpServletRequestBuilder comCorpo(MockHttpServletRequestBuilder req, Object corpo) throws Exception {
        return req.contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(corpo));
    }

    protected JsonNode lerJson(String texto) throws Exception {
        return json.readTree(texto);
    }

    private String token(MockHttpServletRequestBuilder req, Object corpo, int esperado) throws Exception {
        String resposta = mvc.perform(comCorpo(req, corpo))
                .andExpect(status().is(esperado))
                .andReturn().getResponse().getContentAsString();
        return lerJson(resposta).get("token").asText();
    }
}
