package br.com.diariotreino.core.exercicio.controllers;

import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioInDTO;
import br.com.diariotreino.core.exercicio.domains.dtos.ExercicioOutDTO;
import br.com.diariotreino.core.exercicio.services.ExercicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Exercícios", description = "RF03: catálogo e exercícios personalizados")
@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final ExercicioService service;

    @Operation(summary = "Lista o catálogo e os exercícios personalizados do usuário")
    @GetMapping
    public List<ExercicioOutDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Cria um exercício personalizado")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExercicioOutDTO criar(@Valid @RequestBody ExercicioInDTO dto) {
        return service.criar(dto);
    }
}
