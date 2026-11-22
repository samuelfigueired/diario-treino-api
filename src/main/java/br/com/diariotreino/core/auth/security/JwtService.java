package br.com.diariotreino.core.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;

/** Gera e lê o token de acesso (JWT assinado com HMAC). */
@Service
public class JwtService {

    static final String SEGREDO_PADRAO = "troque-este-segredo-em-producao-0123456789abcdef";
    private static final int TAMANHO_MINIMO_SEGREDO = 32;

    private final SecretKey key;
    private final long expiracaoMinutos;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiracao-minutos}") long expiracaoMinutos,
                      Environment ambiente) {
        validarSegredo(secret, ambiente.acceptsProfiles(Profiles.of("prod")));
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMinutos = expiracaoMinutos;
    }

    /** Em produção o segredo padrão (público, está no código) deixaria qualquer um forjar um login. */
    static void validarSegredo(String secret, boolean producao) {
        if (secret == null || secret.length() < TAMANHO_MINIMO_SEGREDO) {
            throw new IllegalStateException("app.jwt.secret precisa ter pelo menos 32 caracteres");
        }
        if (producao && SEGREDO_PADRAO.equals(secret)) {
            throw new IllegalStateException("Defina APP_JWT_SECRET: o segredo padrão não pode ser usado em produção.");
        }
    }

    public String gerar(UsuarioPrincipal u) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(u.getId()))
                .claim("nome", u.getNome())
                .claim("email", u.getEmail())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plus(expiracaoMinutos, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    /** Lança exceção se o token for inválido ou estiver expirado. */
    public UsuarioPrincipal ler(String token) {
        Claims c = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return new UsuarioPrincipal(Long.valueOf(c.getSubject()), c.get("nome", String.class), c.get("email", String.class));
    }
}
