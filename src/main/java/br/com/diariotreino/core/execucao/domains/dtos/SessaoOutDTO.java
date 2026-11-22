package br.com.diariotreino.core.execucao.domains.dtos;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Sessão devolvida pela API (histórico e evolução). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoOutDTO {

    private Long id;

    private Long treinoId;

    private String nomeTreino;

    private String letraTreino;

    private Instant inicio;

    private Instant fim;

    private List<SerieOutDTO> series;
}
