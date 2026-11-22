package br.com.diariotreino.core.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.diariotreino.ApiTestBase;
import br.com.diariotreino.core.config.DadosDemonstracao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class AuthApiTest extends ApiTestBase {

    @Test
    @DisplayName("O login com e-mail e senha corretos devolve o token e os dados do usuário, sem a senha")
    void deveDevolverTokenQuandoCredenciaisEstiveremCorretas() throws Exception {
        // given
        var corpo = Map.of("email", DadosDemonstracao.EMAIL_DEMO, "senha", DadosDemonstracao.SENHA_DEMO);

        // when
        var resultado = mvc.perform(comCorpo(post("/api/auth/login"), corpo));

        // then
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.usuario.nome").value("Mariana Alves Rocha"))
                .andExpect(jsonPath("$.usuario.senhaHash").doesNotExist());
    }

    @Test
    @DisplayName("O login com senha errada responde 400 com mensagem que não revela se o e-mail existe")
    void deveRecusarLoginQuandoSenhaEstiverErrada() throws Exception {
        // given
        var corpo = Map.of("email", "camila.duarte." + System.nanoTime() + "@exemplo.com.br", "senha", "senha-errada");

        // when
        var resultado = mvc.perform(comCorpo(post("/api/auth/login"), corpo));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("E-mail ou senha incorretos."));
    }

    @Test
    @DisplayName("Não é possível criar duas contas com o mesmo e-mail, mesmo mudando maiúsculas")
    void deveRecusarCadastroQuandoEmailJaExistir() throws Exception {
        // given
        var corpo = Map.of("nome", "Mariana Rocha", "email", "Mariana.Rocha@Exemplo.com.br", "senha", "outrasenha123");

        // when
        var resultado = mvc.perform(comCorpo(post("/api/auth/cadastro"), corpo));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Já existe uma conta com este e-mail. Entre com sua senha."));
    }

    @Test
    @DisplayName("O cadastro com senha curta é recusado e aponta o campo com a mensagem")
    void deveApontarCampoQuandoSenhaForCurta() throws Exception {
        // given
        var corpo = Map.of("nome", "Rafael Gomes", "email", "rafael.gomes." + System.nanoTime() + "@exemplo.com.br", "senha", "1234");

        // when
        var resultado = mvc.perform(comCorpo(post("/api/auth/cadastro"), corpo));

        // then
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.senha").value("A senha precisa ter pelo menos 8 caracteres."));
    }

    @Test
    @DisplayName("A rota /me devolve o usuário dono do token")
    void deveDevolverUsuarioLogadoQuandoChamarMe() throws Exception {
        // given
        String token = tokenDemo();

        // when
        var resultado = mvc.perform(comToken(get("/api/auth/me"), token));

        // then
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.email").value(DadosDemonstracao.EMAIL_DEMO));
    }
}
