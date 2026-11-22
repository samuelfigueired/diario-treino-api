package br.com.diariotreino.core.execucao.domains.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Treino realizado, enviado pelo app ao finalizar. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoInDTO {

    @NotNull(message = "Informe o treino realizado.")
    private Long treinoId;

    @NotNull
    private Instant inicio;

    @NotNull
    private Instant fim;

    @NotEmpty(message = "Conclua pelo menos uma série para salvar o treino.")
    @Valid
    private List<SerieInDTO> series;
}
