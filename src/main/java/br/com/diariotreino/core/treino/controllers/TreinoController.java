package br.com.diariotreino.core.treino.controllers;

import br.com.diariotreino.core.treino.domains.dtos.TreinoInDTO;
import br.com.diariotreino.core.treino.domains.dtos.TreinoOutDTO;
import br.com.diariotreino.core.treino.services.TreinoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Treinos", description = "RF02 e RF03: treinos e exercícios associados")
@RestController
@RequestMapping("/api/treinos")
@RequiredArgsConstructor
public class TreinoController {

    private final TreinoService service;

    @Operation(summary = "Lista os treinos do usuário, com a última carga de cada exercício")
    @GetMapping
    public List<TreinoOutDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TreinoOutDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreinoOutDTO criar(@Valid @RequestBody TreinoInDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public TreinoOutDTO atualizar(@PathVariable Long id, @Valid @RequestBody TreinoInDTO dto) {
        return service.atualizar(id, dto);
    }

    @Operation(summary = "Exclui o treino (as sessões já feitas continuam no histórico)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
