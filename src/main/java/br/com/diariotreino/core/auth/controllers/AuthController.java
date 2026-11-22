package br.com.diariotreino.core.auth.controllers;

import br.com.diariotreino.core.auth.domains.dtos.CadastroInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginOutDTO;
import br.com.diariotreino.core.auth.services.AuthService;
import br.com.diariotreino.core.usuario.domains.dtos.UsuarioOutDTO;

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

@Tag(name = "Autenticação", description = "RF01: cadastro e login")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Entra com e-mail e senha e devolve o token de acesso")
    @PostMapping("/login")
    public LoginOutDTO login(@Valid @RequestBody LoginInDTO dto) {
        return service.login(dto);
    }

    @Operation(summary = "Cria a conta e já devolve o token de acesso")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginOutDTO cadastrar(@Valid @RequestBody CadastroInDTO dto) {
        return service.cadastrar(dto);
    }

    @Operation(summary = "Dados do usuário logado")
    @GetMapping("/me")
    public UsuarioOutDTO me() {
        return service.me();
    }
}
