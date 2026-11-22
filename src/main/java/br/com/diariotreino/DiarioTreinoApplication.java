package br.com.diariotreino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/** Sem usuário em memória do Spring: a autenticação é feita pelo JWT (core/auth). */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class DiarioTreinoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiarioTreinoApplication.class, args);
    }
}
