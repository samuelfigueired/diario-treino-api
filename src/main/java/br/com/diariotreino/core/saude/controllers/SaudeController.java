package br.com.diariotreino.core.saude.controllers;

import br.com.diariotreino.core.saude.domains.dtos.SaudeOutDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "Saúde", description = "Verifica se a API está no ar")
@RestController
@RequestMapping("/api/saude")
public class SaudeController {

    @GetMapping
    public SaudeOutDTO verificar() {
        return new SaudeOutDTO("ok", Instant.now());
    }
}
