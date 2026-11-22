package br.com.diariotreino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Sem usuário em memória: a autenticação será feita pelo módulo autenticacao (JWT). */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableJpaAuditing
public class DiarioTreinoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiarioTreinoApplication.class, args);
    }
}
