package br.com.diariotreino.core.auth.services.impl;

import br.com.diariotreino.core.auth.domains.dtos.CadastroInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginInDTO;
import br.com.diariotreino.core.auth.domains.dtos.LoginOutDTO;
import br.com.diariotreino.core.auth.security.JwtService;
import br.com.diariotreino.core.auth.security.LimitadorTentativas;
import br.com.diariotreino.core.auth.security.UsuarioPrincipal;
import br.com.diariotreino.core.auth.services.AuthService;
import br.com.diariotreino.core.entidades.Usuario;
import br.com.diariotreino.core.exception.BadRequestException;
import br.com.diariotreino.core.exception.NotFoundException;
import br.com.diariotreino.core.usuario.domains.dtos.UsuarioOutDTO;
import br.com.diariotreino.core.usuario.domains.mappers.UsuarioMapper;
import br.com.diariotreino.core.usuario.repositorys.UsuarioRepository;
import br.com.diariotreino.core.util.CurrentUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServicePadrao implements AuthService {

    private static final String MENSAGEM_CREDENCIAIS = "E-mail ou senha incorretos.";
    private static final String MENSAGEM_EMAIL_EM_USO = "Já existe uma conta com este e-mail. Entre com sua senha.";

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final LimitadorTentativas limitador;

    @Override
    @Transactional(readOnly = true)
    public LoginOutDTO login(LoginInDTO dto) {
        String email = normalizar(dto.getEmail());
        limitador.verificar(email);
        Usuario usuario = repository.findByEmailIgnoreCase(email)
                .filter(u -> encoder.matches(dto.getSenha(), u.getSenhaHash()))
                .orElseThrow(() -> {
                    limitador.registrarFalha(email);
                    return new BadRequestException(MENSAGEM_CREDENCIAIS);
                });
        limitador.limpar(email);
        return sessao(usuario);
    }

    @Override
    @Transactional
    public LoginOutDTO cadastrar(CadastroInDTO dto) {
        String email = normalizar(dto.getEmail());
        if (repository.existsByEmailIgnoreCase(email)) throw new BadRequestException(MENSAGEM_EMAIL_EM_USO);
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(encoder.encode(dto.getSenha()));
        Usuario salvo = repository.save(usuario);
        log.info("Conta criada: usuario {}", salvo.getId());
        return sessao(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioOutDTO me() {
        Long id = CurrentUser.usuarioId();
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new NotFoundException("Usuário", id));
    }

    private LoginOutDTO sessao(Usuario u) {
        String token = jwt.gerar(new UsuarioPrincipal(u.getId(), u.getNome(), u.getEmail()));
        return new LoginOutDTO(token, mapper.toDto(u));
    }

    private static String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
