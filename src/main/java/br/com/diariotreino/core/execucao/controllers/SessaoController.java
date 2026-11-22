package br.com.diariotreino.core.execucao.controllers;

import br.com.diariotreino.core.execucao.domains.dtos.SessaoInDTO;
import br.com.diariotreino.core.execucao.domains.dtos.SessaoOutDTO;
import br.com.diariotreino.core.execucao.services.SessaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Sessões de treino", description = "RF04 e RF05: registro e histórico dos treinos realizados")
@RestController
@RequestMapping("/api/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService service;

    @Operation(summary = "Lista as sessões do usuário (padrão: último ano), mais recentes primeiro")
    @GetMapping
    public List<SessaoOutDTO> listar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde) {
        return service.listar(desde);
    }

    @GetMapping("/{id}")
    public SessaoOutDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @Operation(summary = "Registra o treino realizado com as séries")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoOutDTO registrar(@Valid @RequestBody SessaoInDTO dto) {
        return service.registrar(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
