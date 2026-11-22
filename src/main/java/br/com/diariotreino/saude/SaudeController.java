package br.com.diariotreino.saude;

import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Verificação simples de que a API está no ar (usada pelo app e pelo deploy). */
@RestController
@RequestMapping("/api/saude")
public class SaudeController {

    @GetMapping
    public SaudeResponse verificar() {
        return new SaudeResponse("ok", Instant.now());
    }

    public record SaudeResponse(String status, Instant horario) {
    }
}
