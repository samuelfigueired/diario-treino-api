package br.com.diariotreino.core.saude.domains.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaudeOutDTO {

    private String status;

    private Instant horario;
}
